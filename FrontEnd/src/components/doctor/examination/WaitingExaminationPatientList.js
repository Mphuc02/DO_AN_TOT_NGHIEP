import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {ExaminationResult, PATIENT} from "../../../ApiConstant";
import styles from '../../../layouts/body/style.module.css'
import {useNavigate} from "react-router-dom";
import RoutesConstant from "../../../RoutesConstant";
import {FormatDate} from "../../../service/TimeService";

const WaitingExaminationPatientList = () => {
    const [patientsMap, setPatientMap] = useState(new Map())
    const [waitingPatients, setWaitingPatient] = useState([])
    const navigate = useNavigate();

    const getPatientsByIds = (ids) => {
        const tempMap = new Map()
        SendApiService.postRequest(PATIENT.PATIENT_API.getByIds(), [...ids], {}, (response) => {
            for (let patient of response.data) {
                tempMap.set(patient.id, patient)
            }
            setPatientMap(tempMap)
        }, (error) => {

        })
    }

    const getPatients = () => {
        SendApiService.getRequest(ExaminationResult.ExaminationResultUrl.findWaitingExaminationPatients(), {}, (response) => {
            const patientIds = []
            for(const patient of response.data){
                patientIds.push(patient.patientId)
            }
            setWaitingPatient(response.data)
            getPatientsByIds(patientIds)
        }, (error) => {

        })
    }

    const connectToWebsocket = () => {

    }

    useEffect(() => {
        getPatients()
        connectToWebsocket()
    }, []);

    return (
        <div>
            <table className="table-auto border-collapse border border-gray-300 w-full text-left"
                border={1}>
                <thead className="bg-gray-200">
                    <tr>
                        <td className="border border-gray-300 px-4 py-2">Số thứ tự</td>
                        <td className="border border-gray-300 px-4 py-2">Tên bệnh nhân</td>
                        <td className="border border-gray-300 px-4 py-2">Ngày sinh</td>
                        <td className="border border-gray-300 px-4 py-2">Giới tính</td>
                    </tr>
                </thead>
                <tbody>
                {waitingPatients.map(function (waitingPatient, index) {
                    const patient = patientsMap.get(waitingPatient.patientId)
                    if (!patient) {
                        return ''
                    }


                    const fullName = patient.fullName
                    const url = RoutesConstant.DOCTOR.EXAMINING_PATIENT_WITH_ID(waitingPatient.id)
                    console.log(url)
                    return <tr key={waitingPatient.id} onClick={() => navigate(url)} className={styles.cursorPointer}>
                                <td className="border border-gray-300 px-4 py-2 text-center">{waitingPatient.examinedNumber}</td>
                                <td className="border border-gray-300 px-4 py-2 text-center">{fullName.lastName + ' ' + fullName.middleName + ' ' + fullName.firstName}</td>
                                <td className="border border-gray-300 px-4 py-2 text-center">{FormatDate(patient.dateOfBirth)}</td>
                                <td className="border border-gray-300 px-4 py-2 text-center">{patient.gender === 1 ? 'Nam' : 'Nữ'}</td>
                            </tr>
                })}
                </tbody>
            </table>
        </div>
    )
}

export {WaitingExaminationPatientList}