import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {ExaminationResult, PATIENT} from "../../../ApiConstant";
import RoutesConstant from "../../../RoutesConstant";
import {Link} from "react-router-dom";
import styles from '../../../layouts/body/style.module.css'

const HistoriesExamiantion = () => {

}

const Examination = ({examinationResult, patient, imageDetails}) => {

    return (
        <div>
            <table>
                <tbody>
                    <tr>
                        <td>Bệnh nhân:</td>
                        <td>${}</td>
                    </tr>

                    <tr>
                        <td>Triệu chứng: </td>
                        <td>{examinationResult.symptom}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr><td>Hình ảnh chuẩn đoán</td></tr>
                    <tr>
                        <td>Hình ảnh</td>
                        <td>Kết quả chuẩn đoán</td>
                    </tr>
                </thead>
                <tbody>
                    {examinationResult.images.map(image => {
                        return <tr key={image.id}>
                            <td><img className={styles.previewImage} src={image.image}/></td>
                            <td><img className={styles.previewImage} src={image.processedImage}/></td>
                        </tr>
                    })}
                </tbody>
            </table>
        </div>
    )
}

const ExaminatingPatient = () => {
    const examintionResultId = window.location.href.split('/').pop()
    const [examinationResult, setExaminationResult] = useState({})
    const [patient, setPatient] = useState({})
    const [selectedTab, setSeletedTab] = useState(1)
    const [imageDetails, setImageDetails] = useState([])

    const getImageDetails = (id, examinationResult) => {
        SendApiService.getRequest(PATIENT.APPOINTMENT.findAppointmentDetailByAppointmentId(id), {}, response => {
            examinationResult.images = response.data
        }, error => {

        })
    }

    const getPatient = (patientId, examinationResult) => {
        SendApiService.getRequest(PATIENT.PATIENT_API.findById(patientId), {}, response => {
            examinationResult.patient = response.data
        }, error => {

        })
    }

    const getExamiantionResult = () => {
        SendApiService.getRequest(ExaminationResult.ExaminationResultUrl.findById(examintionResultId), {}, response => {
            const tempResult = response.data
            getPatient(tempResult.patientId, tempResult)
            if(response.data.appointmentId){
                getImageDetails(response.data.appointmentId, tempResult)
            }
            setExaminationResult(tempResult)
        }, error => {

        })
    }

    useEffect(() => {
        getExamiantionResult()
    }, []);

    return (
        <div>
            <Link to={RoutesConstant.DOCTOR.EXAMINATION_MANAGEMENT}>Quay lại danh sách bệnh nhân chờ khám</Link>
            <h2>Khám bệnh cho bệnh nhân</h2>

            <div className={styles.divFlex}>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(1)}>Khám bệnh</div>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(2)}>Các lần khám trước</div>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(3)}>Phiếu tư vấn thuốc</div>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(4)}>Tạo phiếu hẹn khám lại</div>
            </div>

            {selectedTab === 1 && <Examination examinationResult={examinationResult} patient={patient} imageDetails={imageDetails}/>}
        </div>
    )
}

export {ExaminatingPatient}