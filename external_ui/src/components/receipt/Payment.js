import {useEffect, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {ExaminationResult, HOSPITAL_INFORMATION, PATIENT, PaymentApi} from "../../ApiConstant";
import {FormatCreatedDate} from "../../service/TimeService";
import {Link, useNavigate} from "react-router-dom";
import RoutesConstant from "../../RoutesConstant";

const PayForInvoice = (id) => {
    const [invoice, setInvoice] = useState({})
    const [consultedMedicinesMap, setConsultedMedicinesMap] = useState(new Map())
    const invoiceId = window.location.href.split('/').pop()

    const getAddressDetail = (patient) => {
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(HOSPITAL_INFORMATION.ADDRESS.getDetail(), patient.address, {}, response => {
                patient.address = {...response.data, ...patient.address, }
                resolve()
            }, error => {
                reject()
            })
        })
    }

    const getMedicineConsultationForm = (id) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(ExaminationResult.MedicineConsultationForm.byId(id), {}, response => {
                const tempMap = new Map()
                for(const medicine of response.data.details){
                    tempMap.set(medicine.medicineId, medicine)
                }
                setConsultedMedicinesMap(tempMap)
                resolve()
            },error => {
                reject()
            })
        })
    }

    const getPatient = (patientId, tempInvoice) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(PATIENT.PATIENT_API.byId(patientId), {}, async response => {
                tempInvoice.patient = response.data
                await getAddressDetail(response.data)
                resolve()
            }, error => {
                reject()
            })
        })
    }

    const getInvoice = (id) => {
        SendApiService.getRequest(PaymentApi.byId(id), {}, async response => {
            const tempInvoice = response.data
            await getPatient(tempInvoice.patientId, tempInvoice)
            await getMedicineConsultationForm(id)
            setInvoice(tempInvoice)
        }, error => {

        })
    }

    useEffect(() => {
        getInvoice(invoiceId)
    }, [])

    const onChangeQuantityMedicine = (quantity, medicineId) => {
        const newMedicine = consultedMedicinesMap.get(medicineId)
        consultedMedicinesMap.set(medicineId, {...newMedicine, quantity: quantity})
        setConsultedMedicinesMap(new Map(consultedMedicinesMap))
    }

    const onRemoveMedicine = (id) => {
        consultedMedicinesMap.delete(id)
        setConsultedMedicinesMap(new Map(consultedMedicinesMap))
    }

    const handlePayIncCash = () => {
        const details = []
        for(const [key,value] of [...consultedMedicinesMap]){
            details.push({
                medicineId: key,
                quantity: value.quantity
            })
        }

        const invoice = {
            details: details
        }

        SendApiService.putRequest(PaymentApi.payInCash(invoiceId), invoice, {}, response => {

        }, error => {

        })
    }

    return (
        <div>
            <h2 className="text-xl font-bold text-green-600 mb-4">
                Thanh toán hoá đơn
                <span className="text-xl font-bold text-green-600 mb-4"> ⟶ hóa đơn của {(() => {
                    if(!invoice.patient){
                        return null
                    }

                    const fullName = invoice.patient.fullName
                    return `${fullName.lastName} ${fullName.middleName} ${fullName.firstName}`
                })()}</span>
            </h2>
            <Link to={RoutesConstant.RECEIPT.PAYMENT}>Quay lại trang thanh toán</Link>

            <table>
                <thead>
                    <tr></tr>
                </thead>
                <tbody>
                <tr>
                    <td colSpan="2"
                        className="bg-blue-100 text-left text-lg font-semibold p-3 border-b border-gray-300"><h3>Thông
                        tin liên hệ</h3></td>
                </tr>

                {(() => {
                    if (!invoice.patient) {
                        return null
                    }
                    const patient = invoice.patient
                    const fullName = invoice.patient.fullName
                    const address = invoice.patient.address
                    return <>
                        <tr>
                            <td>Tên bệnh nhân:</td>
                            <td>{fullName.lastName} {fullName.middleName} {fullName.firstName}</td>
                        </tr>
                        <tr>
                            <td>Địa chỉ:</td>
                            <td>{address.street} - {address.communeName} - {address.districtName} - {address.provinceName}</td>
                        </tr>
                        <tr>
                            <td>Ngày sinh:</td>
                            <td>{patient.dateOfBirth}</td>
                        </tr>
                        <tr>
                            <td>Giới tính:</td>
                            <td>{patient.gender}</td>
                        </tr>
                        <tr>
                            <td>Phí khám bệnh:</td>
                            <td>{invoice.examinationCost}</td>
                        </tr>
                    </>
                })()}

                    <tr>
                        <td colSpan="2"
                            className="bg-blue-100 text-left text-lg font-semibold p-3 border-b border-gray-300"><h3>Danh
                            sách thuốc được tư vấn</h3></td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <td>Số thứ tự</td>
                        <td>Tên thuốc</td>
                        <td>Số lượng</td>
                        <td>Đơn giá</td>
                        <td>Thành tiền</td>
                    </tr>
                </thead>
                <tbody>
                    {[...consultedMedicinesMap].map(([key, value], index) => {
                        return <tr key={key}>
                                    <td>{index + 1}</td>
                                    <td></td>
                                    <td><input type={"number"} value={value.quantity} onChange={e => onChangeQuantityMedicine(e.target.value, key)}/></td>
                                    <td></td>
                                    <td></td>
                                    <td><button onClick={() => onRemoveMedicine(key)}>Bỏ chọn thuốc này</button></td>
                        </tr>
                    })}
                </tbody>
            </table>

            {!invoice.paidAt && <button onClick={() => handlePayIncCash()}>Thanh toán</button>}
        </div>
    )
}

const UnPaidInvoice = () => {
    const [invoicesMap, setInvoiceMap] = useState(new Map())
    const navigate = useNavigate()

    const getPatients = (patientMap) => {
        return new Promise((resolve, reject) => {
            const ids = [...patientMap.keys()]
            SendApiService.postRequest(PATIENT.PATIENT_API.getByIds(), ids, {}, response => {
                for(const patient of response.data){
                    const invoice = patientMap.get(patient.id)
                    invoice.patient = patient
                }
                resolve()
            }, error => {
                reject()
            })
        })
    }

    const getUnpaidInvoice = () => {
        SendApiService.getRequest(PaymentApi.getUnPaid(), {}, async response => {
            const tempMap = new Map()
            const patientMap = new Map()
            for(const invoice of response.data.content){
                tempMap.set(invoice.id, invoice)
                patientMap.set(invoice.patientId, invoice)
            }

            await getPatients(patientMap)
            setInvoiceMap(tempMap)
        }, error => {

        })
    }

    useEffect(() => {
        getUnpaidInvoice()
    }, []);

    return (
        <table border={1}>
            <thead>
                <tr>
                    <td>Số thứ tự</td>
                    <td>Tên bệnh nhận</td>
                    <td>Giá khám bệnh</td>
                    <td>Thời gian tạo hóa đơn</td>
                </tr>
            </thead>
            <tbody>
                {[...invoicesMap].map(([key, value], index) => {
                    if(!value.patient){
                        return null
                    }

                    const fullName = value.patient.fullName
                    const fullNameStr = `${fullName.lastName} ${fullName.middleName} ${fullName.firstName}`

                    return <tr key={key}
                               className="hover:cursor-pointer hover:bg-gray-100 transition"
                                onClick={() => navigate(RoutesConstant.RECEIPT.payForInvoice(key))}>
                                <td>{index + 1}</td>
                                <td>{fullNameStr}</td>
                                <td>{value.examinationCost}</td>
                                <td>{FormatCreatedDate(value.createdAt)}</td>
                            </tr>
                })}
            </tbody>
        </table>
    )
}

const Payment = () => {
    const [selectedTab, setSelectedTab] = useState(1)

    return (
        <div>
            <h2 className="text-xl font-bold text-green-600 mb-4">Thanh toán hoá đơn</h2>

            <div className="flex space-x-4 mb-4">
                <div
                    className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 1 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(1)}>
                    Danh sách hóa đơn chưa thanh toán
                </div>
                <div
                    className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 2 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(2)}>
                    Danh sách hóa đơn đã thanh toán
                </div>
            </div>

            {selectedTab === 1 && <UnPaidInvoice />}
        </div>
    )
}

export {Payment, PayForInvoice}