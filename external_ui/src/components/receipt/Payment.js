import {useEffect, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {ExaminationResult, PATIENT, PaymentApi} from "../../ApiConstant";
import {FormatCreatedDate} from "../../service/TimeService";
import {useNavigate} from "react-router-dom";
import RoutesConstant from "../../RoutesConstant";

const PayForInvoice = (id) => {
    const [invoice, setInvoice] = useState({})

    const getMedicineConsultationForm = (id) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(ExaminationResult.MedicineConsultationForm.byId(id), {}, response => {

            },error => {

            })
        })
    }

    const getPatient = (patientId, invoice) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(PATIENT.PATIENT_API.byId(patientId), {}, response => {
                invoice.patient = response.data
                resolve()
            }, error => {

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
        const invoiceId = window.location.href.split('/').pop()
        getInvoice(invoiceId)

    }, [])

    return (
        <div>
            <h2 className="text-xl font-bold text-green-600 mb-4">
                Thanh toán hoá đơn
                <span className="text-xl font-bold text-green-600 mb-4"> ⟶ hóa đơn của</span>
            </h2>


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