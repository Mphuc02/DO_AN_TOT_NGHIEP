import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {EMPLOYYEE, ExaminationResult, MEDICINE, PATIENT} from "../../../ApiConstant";
import {FormatCreatedDate} from "../../../service/TimeService";

const MedicineConsultationModal = ({isOpen, onClose, id}) => {
    const [form, setForm] = useState(null)
    const [medicineMap, setMedicineMap] = useState(new Map())

    const getMedicineMaps = (form) => {
        const medicineSet = new Map()
        for(const detail of form.details){
            medicineSet.set(detail.medicineId, detail)
        }

        return new Promise((resolve, reject) => {
            SendApiService.postRequest(MEDICINE.Medicine.getByIDs(), [...medicineSet.keys()], {}, response => {
                for(const medicine of response.data){
                    medicineSet.get(medicine.id).medicine = medicine
                }
                resolve()
            }, error => {
                reject()
            })
        })
    }

    const getConsultationForm = () => {
        SendApiService.getRequest(ExaminationResult.MedicineConsultationForm.byId(id), {}, async response => {
            if(!response.data){
                return
            }
            const tempForm = response.data
            await getMedicineMaps(tempForm)
            setForm(tempForm)
        }, error => {

        })
    }

    useEffect(() => {
        getConsultationForm()
    }, [id])

    if (!isOpen) {
        return null;
    }

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-full sm:w-96 relative">
                <button
                    className="absolute top-2 right-2 text-3xl text-red-600 hover:text-red-800"
                    onClick={onClose}>
                    &times;
                </button>

                {!form && <h1 className="text-2xl font-semibold text-green-600 mb-4 text-center">
                    Lần khám bệnh này không có phiếu tư vấn thuốc
                </h1>}

                {form && <>
                    <h1 className="text-2xl font-semibold text-green-600 mb-4 text-center">
                        Xem chi tiết phiếu tư vấn thuốc
                    </h1>

                    <table className="table-auto border-collapse border border-gray-300 w-full text-left">
                        <thead className="bg-gray-200">
                        <tr>
                            <td className="border border-gray-300 px-4 py-2">Số thứ tự</td>
                            <td className="border border-gray-300 px-4 py-2">Tên thuốc</td>
                            <td className="border border-gray-300 px-4 py-2">Cách dùng</td>
                            <td className="border border-gray-300 px-4 py-2">Số lượng tư vấn</td>
                        </tr>
                        </thead>
                        <tbody>
                        {form && form.details.map((detail, index) => {
                            if (!detail) {
                                return null
                            }
                            return <tr key={detail.id}>
                                <td>{index + 1}</td>
                                <td>{detail.medicine.name}</td>
                                <td>{detail.treatment}</td>
                                <td>{detail.quantity}</td>
                            </tr>
                        })}
                        </tbody>
                    </table>
                </>}

            </div>
        </div>
    );
};

const PatientExaminationHistories = ({examinationResult}) => {
    const [histories, setHistories] = useState([])
    const [isOpen, setIsOpen] = useState(null)
    const [examinationResultId, setExaminationResultId] = useState(null)
    const [patient, setPatient] = useState(null)

    const getPatient = () => {
        SendApiService.getRequest(PATIENT.PATIENT_API.byId(examinationResult.patientId), {} , response => {
            setPatient(response.data)
        }, error => {

        })
    }

    const getHistories = () => {
        SendApiService.getRequest(ExaminationResult.ExaminationResultUrl.findHistoriesByPatientId(examinationResult.patientId), {}, async response => {
            const doctorIdSet = new Set()
            const tempHistories = response.data
            for (const result of response.data) {
                doctorIdSet.add(result.employeeId)
            }

            console.log(tempHistories)
            setHistories(tempHistories)
        }, error => {

        })
    }

    const onOpenMedicineModal = (id) => {
        setIsOpen(true)
        setExaminationResultId(id)
    }

    const onCloseMedicineModal = () => {
        setIsOpen(false)
    }

    useEffect(() => {
        getPatient()
        getHistories()
    }, [examinationResult]);

    return (<div>
            <div className={"mt-10"}>
                <ul className="space-y-4 ml-5 mr-5">
                    {histories.map((record, index) => {
                        if(!patient){
                            return null
                        }

                        return <li
                            onClick={() => onOpenMedicineModal(record.id)}
                            key={record.id}
                            className="p-4 bg-white border border-black rounded-lg shadow-md hover:shadow-lg transition-shadow hover:cursor-pointer">
                            <div className="flex justify-between">
                                <div>
                                    <h3 className="text-xl font-semibold text-gray-800">
                                        {`${patient.fullName.lastName} ${patient.fullName.middleName} ${patient.fullName.firstName}`}
                                    </h3>
                                    <p className="text-sm text-gray-500">Mã lịch hẹn: {record.id}</p>
                                    <p className="text-sm text-gray-500">
                                        Ngày tạo: {FormatCreatedDate(record.createdAt)}
                                    </p>
                                    <p className="text-sm text-gray-500">
                                        Ngày khám: {FormatCreatedDate(record.examinatedAt)}
                                    </p>
                                </div>
                            </div>
                            <div className="mt-2">
                                <p className="text-gray-800">
                                    <span className="font-medium">Triệu chứng:</span> {record.symptom}
                                </p>
                                <p className="text-gray-800">
                                    <span className="font-medium">Điều trị:</span> {record.treatment}
                                </p>
                            </div>
                        </li>
                    })}
                </ul>
            </div>
            <MedicineConsultationModal isOpen={isOpen} onClose={onCloseMedicineModal} id={examinationResultId}/>
        </div>
    )
}

export {PatientExaminationHistories}