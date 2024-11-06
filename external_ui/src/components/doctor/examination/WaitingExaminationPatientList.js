import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {ExaminationResult, PATIENT} from "../../../ApiConstant";
import styles from '../../../layouts/body/style.module.css'
import {useNavigate} from "react-router-dom";

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
            <table border={1}>
                <thead>
                    <tr>
                        <td>Số thứ tự</td>
                        <td>Tên bệnh nhân</td>
                    </tr>
                </thead>
                <tbody>
                {waitingPatients.map((waitingPatient, index) => {
                        const patient = patientsMap.get(waitingPatient.patientId)
                        if(!patient){
                            return ''
                        }
                        const fullName = patient.fullName
                        return <tr onClick={() => navigate('/hehe')} className={styles.cursorPointer}>
                                    <td>{waitingPatient.examinedNumber}</td>
                                    <td>{fullName.firstName + ' ' + fullName.middleName + ' ' + fullName.lastName}</td>
                                </tr>
                    })}
                </tbody>
            </table>
        </div>
    )
}

export {WaitingExaminationPatientList}