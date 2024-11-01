import {useState, useEffect} from "react";
import {SendApiService} from "../../service/SendApiService";
import {Greeting, HOSPITAL_INFORMATION, PATIENT} from "../../ApiConstant";
import styles from '../../layouts/body/style.module.css'

const CreateExaminationFormModal = ({ isOpen, onClose, appointment, roomsMap, workingSchedule, diseasesMap }) => {
    const [createExaminationForm, setCreateExaminationForm] = useState({...appointment, symptom: appointment.description})

    console.log('appointment', appointment)

    useEffect(() => {
        setCreateExaminationForm({...appointment, symptom: appointment.description})
    },[appointment])

    if(!isOpen){
        return null
    }

    const handleCreateExaminationForm = () => {
        //Todo: Sau bỏ fix cứng số thứ tự
        createExaminationForm.numberCall = 1
        SendApiService.postRequest(Greeting.ExaminationForm.getGetUrl(), createExaminationForm, {'Content-type': 'application/json'}, (response) => {

        }, (error) => {

        })
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>

                <table>
                    <thead>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Tên bệnh nhân:</td>
                        <td>
                            {appointment.fullName.firstName + " " + appointment.fullName.middleName + " " + appointment.fullName.lastName}
                        </td>
                    </tr>

                    <tr>
                        <td>Triệu chứng:</td>
                        <td>{appointment.description}</td>
                    </tr>

                    <tr>
                        <td>Chọn phòng khám:</td>
                        <td>
                            <select
                                value={createExaminationForm.workingSchedule || ""}
                                onChange={(e) => setCreateExaminationForm({
                                    ...createExaminationForm,
                                    workingSchedule: e.target.value
                                })}>
                                <option value={""}>-----------</option>
                                {[...workingSchedule].map(([key, value]) => (
                                    <option key={key} value={value.id}>
                                        {(() => {
                                            if(value && value.employeeId === appointment.doctorId){
                                                createExaminationForm.workingSchedule = value.id
                                            }
                                            return roomsMap.get(value.roomId).name
                                        })()}
                                    </option>
                                ))}
                            </select>

                        </td>
                    </tr>

                    <tr>
                        <td>Các bệnh chuẩn đoán:</td>
                        <td>
                            {appointment.details.map(detail => (
                                <li key={detail.id}>
                                    {(() => {
                                        const disease = diseasesMap.get(detail.diseaseId)
                                        if (disease) {
                                            return disease.name
                                        }
                                        return ''
                                    })()}
                                </li>
                            ))}
                        </td>
                    </tr>

                    <tr>
                        <td>Các hình ảnh chuản đoán:</td>
                        <td></td>
                        <td></td>
                    </tr>

                    <tr>
                        <td>Hình ảnh</td>
                        <td>Hình ảnh được chuẩn đoán</td>
                        <td></td>
                    </tr>

                    {appointment.images.map((image, index) => {
                        return (
                            <tr key={index}>
                                <td><img className={styles.previewImage} src={image.image} alt={image.image}/></td>
                                <td><img className={styles.previewImage} src={image.processedImage} alt={image.image}/>
                                </td>
                            </tr>
                        )
                    })}
                    </tbody>
                </table>
                <button onClick={() => handleCreateExaminationForm()}>Tạo phiếu khám bệnh</button>
            </div>
        </div>
    )
}

const ReceiptWithAppointment = ({workingScheduleMap, workingRoomsMap}) => {
    const [appointments, setAppointments] = useState([])
    const [patientsMap, setPatientsMap] = useState(new Map())
    const [isCreateModalOpen, setIsCreateModelOpen] = useState(false);
    const [selectedAppointment, setSelectedAppointment] = useState({})
    const [diseasesMap, setDiseasesMap] = useState(new Map())

    const getPatientsByIds = async (ids) => {
        const tempMap = new Map()
        SendApiService.postRequest(PATIENT.PATIENT_API.getByIds(), [...ids], {}, (response) => {
            for (let patient of response.data) {
                tempMap.set(patient.id, patient)
            }
            setPatientsMap(tempMap)
        }, (error) => {

        })
    }

    const getAppointmentByToday = async () => {
        SendApiService.getRequest(PATIENT.APPOINTMENT.getAppointmentsOfToday(), {}, (response) => {
            setAppointments(response.data)
            const tempPatientSet = new Set()
            for (let appointment of response.data) {
                tempPatientSet.add(appointment.patientId)
            }
            console.log(tempPatientSet)
            setAppointments(response.data)
            getPatientsByIds(tempPatientSet)
        }, (error) => {

        })
    }

    const getDiseases = () => {
        SendApiService.getRequest(HOSPITAL_INFORMATION.DISEASES.getUrl(), {}, (response) => {
            const tempMap = new Map()
            for(const disease of response.data){
                tempMap.set(disease.id, disease)
            }
            setDiseasesMap(tempMap)
        })
    }

    useEffect(() => {
        getAppointmentByToday()
        getDiseases()
    }, [])

    const handleCreateExaminationForm = (appointment) => {
        appointment.fullName = patientsMap.get(appointment.patientId).fullName
        setSelectedAppointment(appointment)
        openCreateModal()
    }

    const openCreateModal = () => {
        setIsCreateModelOpen(true);
    };

    const closeCreateModal = () => {
        setIsCreateModelOpen(false);
    };

    return (
        <div>
            <h2>Tiếp đón với lịch hẹn</h2>
            <table border="1">
                <thead>
                <tr>
                    <td>Số thứ tự</td>
                    <td>Tên bệnh nhân</td>
                    <td>Triệu chứng</td>
                </tr>
                </thead>
                <tbody>
                {appointments.map((appointment, index) => (
                    <tr key={appointment.id} className={styles.cursorPointer}
                        onClick={() => handleCreateExaminationForm(appointment)}>
                        <td></td>
                        <td>{(() => {
                            const patient = patientsMap.get(appointment.patientId)
                            if (!patient)
                                return ''
                            else {
                                const fullName = patient.fullName
                                return `${fullName.firstName || ''} ${fullName.middleName || ''} ${fullName.lastName || ''}`;
                            }
                        })()}</td>
                        <td>{appointment.description}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <CreateExaminationFormModal isOpen={isCreateModalOpen} onClose={closeCreateModal} appointment={selectedAppointment} roomsMap={workingRoomsMap} workingSchedule={workingScheduleMap} diseasesMap={diseasesMap} />
        </div>
    )
}

export {ReceiptWithAppointment}