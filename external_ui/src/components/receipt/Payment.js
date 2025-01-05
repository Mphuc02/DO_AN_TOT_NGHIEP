import {useEffect, useRef, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {ExaminationResult, HOSPITAL_INFORMATION, MEDICINE, PATIENT, PaymentApi, VNPAY} from "../../ApiConstant";
import {FormatCreatedDate, FormatDate} from "../../service/TimeService";
import {Link, useNavigate} from "react-router-dom";
import RoutesConstant from "../../RoutesConstant";

const PayForInvoice = (id) => {
    const [invoice, setInvoice] = useState({})
    const [consultedMedicinesMap, setConsultedMedicinesMap] = useState(new Map())
    const invoiceId = window.location.href.split('/').pop()
    const totalMoney = useRef(0)
    const urlParams = new URLSearchParams(window.location.search);
    const [status, setStatus] = useState(urlParams.get("status"))

        const getMedicines = (medicinesMap) => {
        return new Promise((resolve, reject) => {
            const ids = [...medicinesMap.keys()]
            SendApiService.postRequest(MEDICINE.Medicine.getByIDs(), ids, {}, response => {
                for(const medicine of response.data){
                    medicinesMap.get(medicine.id).medicine = medicine
                }
                resolve()
            }, error => {
                reject(error)
            })
        })
    }

    const getAddressDetail = (patient) => {
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(HOSPITAL_INFORMATION.ADDRESS.getDetail(), patient.address, {}, response => {
                patient.address = {...response.data, ...patient.address, }
                resolve()
            }, error => {
                reject(error)
            })
        })
    }

    const getMedicineConsultationForm = (id) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(ExaminationResult.MedicineConsultationForm.byId(id), {}, async response => {
                const tempMap = new Map()
                if(!response.data){
                    resolve()
                    return
                }
                for(const medicine of response.data.details){
                    tempMap.set(medicine.medicineId, medicine)
                }
                await getMedicines(tempMap)
                setConsultedMedicinesMap(tempMap)
                resolve()
            },error => {
                reject(error)
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
                reject(error)
            })
        })
    }

    const getInvoice = (id) => {
        SendApiService.getRequest(PaymentApi.byId(id), {}, async response => {
            const tempInvoice = response.data
            await getPatient(tempInvoice.patientId, tempInvoice)
            await getMedicineConsultationForm(id)

            totalMoney.current = tempInvoice.examinationCost
            setInvoice(tempInvoice)
        }, error => {

        })
    }

    useEffect(() => {
        getInvoice(invoiceId)
    }, [])

    const onChangeQuantityMedicine = (quantity, medicineId) => {
        const newMedicine = consultedMedicinesMap.get(medicineId)

        totalMoney.current -= newMedicine.quantity * newMedicine.medicine.price
        if(quantity < 0){
            consultedMedicinesMap.set(medicineId, {...newMedicine, quantity: 0})
        }
        else if(quantity > newMedicine.medicine.quantity){
            totalMoney.current += newMedicine.medicine.quantity * newMedicine.medicine.price
            consultedMedicinesMap.set(medicineId, {...newMedicine, quantity: newMedicine.medicine.quantity})
        }else{
            totalMoney.current += quantity * newMedicine.medicine.price
            consultedMedicinesMap.set(medicineId, {...newMedicine, quantity: quantity})
        }
        console.log(totalMoney.current)

        setConsultedMedicinesMap(new Map(consultedMedicinesMap))
    }

    const onRemoveMedicine = (id) => {
        const medicine = consultedMedicinesMap.get(id)
        totalMoney.current -= medicine.quantity * medicine.medicine.price
        consultedMedicinesMap.delete(id)
        setConsultedMedicinesMap(new Map(consultedMedicinesMap))
    }

    const buildInvoice = () => {
        const details = []
        for(const [key,value] of [...consultedMedicinesMap]){
            details.push({
                medicineId: key,
                quantity: value.quantity
            })
        }

        return {
            details: details
        }
    }

    const handlePayInCash = () => {
        const invoice = buildInvoice()

        SendApiService.putRequest(PaymentApi.payInCash(invoiceId), invoice, {}, response => {

        }, error => {

        })
    }

    const handlePayByVnpay = () => {
        const invoice = buildInvoice()

        SendApiService.putRequest(PaymentApi.payByVnpay(invoiceId), invoice, {}, response => {
            window.location.href = response.data
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
            <Link
                to={RoutesConstant.RECEIPT.PAYMENT}
                className="bg-green-500 text-white py-2 px-4 rounded-md hover:bg-green-600 transition duration-300">Quay lại trang thanh toán</Link>

            {(() => {
                if(!status){
                    return null
                }

                return <p className="mt-3 bg-green-500 text-white py-2 px-4 rounded-md hover:bg-green-600 transition duration-300">{VNPAY.errorCode.get(status)}</p>
            })()}
            <table className="table-auto w-full max-w-[800px] border-collapse border border-gray-300 mt-10">
                <thead>
                <tr></tr>
                </thead>
                <tbody>
                <tr>
                    <td colSpan="2"
                        className="bg-green-100 text-left text-lg font-semibold p-3 border-b border-gray-300">
                        <h3>Thông tin liên hệ</h3>
                    </td>
                </tr>

                {(() => {
                    if (!invoice.patient) {
                        return null;
                    }
                    const patient = invoice.patient;
                    const fullName = invoice.patient.fullName;
                    const address = invoice.patient.address;
                    return (
                        <>
                            <tr>
                                <td className="font-medium text-gray-700">Tên bệnh nhân:</td>
                                <td className="text-gray-900">{fullName.lastName} {fullName.middleName} {fullName.firstName}</td>
                            </tr>
                            <tr>
                                <td className="font-medium text-gray-700">Địa chỉ:</td>
                                <td className="text-gray-900">
                                    {address.street} - {address.communeName} - {address.districtName} - {address.provinceName}
                                </td>
                            </tr>
                            <tr>
                                <td className="font-medium text-gray-700">Ngày sinh:</td>
                                <td className="text-gray-900">{FormatDate(patient.dateOfBirth)}</td>
                            </tr>
                            <tr>
                                <td className="font-medium text-gray-700">Giới tính:</td>
                                <td className="text-gray-900">{patient.gender === '1' ? 'Nam' : 'Nữ'}</td>
                            </tr>
                            <tr>
                                <td className="font-medium text-gray-700">Phí khám bệnh:</td>
                                <td className="text-gray-900">{invoice.examinationCost}</td>
                            </tr>
                        </>
                    );
                })()}

                <tr>
                    <td colSpan="2"
                        className="bg-green-100 text-left text-lg font-semibold p-3 border-b border-gray-300">
                        <h3>Danh sách thuốc được tư vấn</h3>
                    </td>
                </tr>
                </tbody>
            </table>

            <table>
                <thead>
                <tr>
                    <td className="border border-gray-300 p-3 text-center bg-green-100 text-green-800 font-semibold">Số
                        thứ tự
                    </td>
                    <td className="border border-gray-300 p-3 text-center bg-green-100 text-green-800 font-semibold">Tên
                        thuốc
                    </td>
                    <td className="border border-gray-300 p-3 text-center bg-green-100 text-green-800 font-semibold">Số
                        lương còn lại
                    </td>
                    <td className="border border-gray-300 p-3 text-center bg-green-100 text-green-800 font-semibold">Số
                        lượng
                    </td>
                    <td className="border border-gray-300 p-3 text-center bg-green-100 text-green-800 font-semibold">Đơn
                        giá
                    </td>
                    <td className="border border-gray-300 p-3 text-center bg-green-100 text-green-800 font-semibold">Thành
                        tiền
                    </td>
                </tr>
                </thead>
                <tbody>
                {[...consultedMedicinesMap].map(([key, value], index) => {
                    console.log('medicine', value)
                    if(!value.medicine){
                        return null
                    }

                    if (totalMoney.current === invoice.examinationCost) {
                        totalMoney.current += value.quantity * value.medicine.price
                    }
                    return <>
                        {/* Hàng hiển thị lỗi */}
                        <tr className="border-b border-gray-300">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>{value.error}</td>
                            <td></td>
                            <td></td>
                        </tr>
                        {/* Hàng hiển thị thuốc */}
                        <tr key={key} className="border-b border-gray-300">
                            <td className="border border-gray-300 p-3 text-center">{index + 1}</td>
                            <td className="border border-gray-300 p-3">{value.medicine.name}</td>
                            <td className="border border-gray-300 p-3 text-center">{value.medicine.quantity}</td>
                            <td className="border border-gray-300 p-3">
                                {!invoice.paidAt ? (
                                    <input
                                        min={1}
                                        type="number"
                                        value={value.quantity}
                                        onChange={(e) => onChangeQuantityMedicine(e.target.value, key)}
                                        max={value.medicine.quantity}
                                        className="w-full p-2 border border-gray-300 rounded-md"
                                    />
                                ) : (
                                    <p>{value.quantity}</p>
                                )}
                            </td>
                            <td className="border border-gray-300 p-3 text-right">{value.medicine.price.toLocaleString()} đ</td>
                            <td className="border border-gray-300 p-3 text-right">
                                {(value.medicine.price * value.quantity).toLocaleString()} đ
                            </td>
                            {!invoice.paidAt && <td className="border border-gray-300 p-3 text-center">
                                <button
                                    onClick={() => onRemoveMedicine(key)}
                                    className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition">Bỏ chọn thuốc này</button>
                            </td>}
                        </tr>
                    </>
                })}
                </tbody>
            </table>

            <p>Tổng tiền thanh toán: {totalMoney.current}</p>
            {!invoice.paidAt &&
                <button className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                        onClick={() => handlePayInCash()}>Thanh toán</button>}
            {!invoice.paidAt &&
                <button className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 mt-10 ml-10" onClick={() => handlePayByVnpay()}>Thanh toán qua ví điện tử VNPAY</button>}
            {invoice.paidAt &&
                <button className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10">In hóa đơn</button>}
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
        <table className="table-auto border-collapse border border-gray-300 w-full text-left">
            <thead className="bg-gray-200">
            <tr>
                <th className="border border-gray-300 px-4 py-2">Số thứ tự</th>
                <th className="border border-gray-300 px-4 py-2">Tên bệnh nhân</th>
                <th className="border border-gray-300 px-4 py-2">Giá khám bệnh</th>
                <th className="border border-gray-300 px-4 py-2">Thời gian tạo hóa đơn</th>
            </tr>
            </thead>
            <tbody>
            {[...invoicesMap].map(([key, value], index) => {
                if (!value.patient) {
                    return null;
                }

                const fullName = value.patient.fullName;
                const fullNameStr = `${fullName.lastName} ${fullName.middleName} ${fullName.firstName}`;

                return (
                    <tr
                        key={key}
                        className="hover:cursor-pointer hover:bg-gray-100 transition duration-200"
                        onClick={() => navigate(RoutesConstant.RECEIPT.payForInvoice(key))}>
                        <td className="border border-gray-300 px-4 py-2 text-center">{index + 1}</td>
                        <td className="border border-gray-300 px-4 py-2">{fullNameStr}</td>
                        <td className="border border-gray-300 px-4 py-2 text-right">
                            {value.examinationCost.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'})}
                        </td>
                        <td className="border border-gray-300 px-4 py-2">{FormatCreatedDate(value.createdAt)}</td>
                    </tr>
                );
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

            {selectedTab === 1 && <UnPaidInvoice/>}
        </div>
    )
}

export {Payment, PayForInvoice}