import {useState, useEffect} from "react";
import {SendApiService} from "../../service/SendApiService";
import {PATIENT} from "../../ApiConstant";
import styles from '../../layouts/body/style.module.css'

const CreateExaminationFormModal = ({ isOpen, onClose, appointment, roomsMap, workingSchedule }) => {
    const [createExaminationForm, setCreateExaminationForm] = useState({...appointment, symptom: appointment.description})
    console.log(appointment)

    if(!isOpen){
        return null
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
                            <td>Chọn phòng khám: </td>
                            <td>
                                <select
                                    onChange={(e) => setCreateExaminationForm({
                                        ...createExaminationForm,
                                        doctorId: e.target.value
                                    })}>
                                    <option value={""}>-----------</option>
                                    {[...workingSchedule].map(([key, value]) => (
                                        <option key={key} value={key}
                                                selected={value.employeeId === appointment.doctorId}>
                                            {roomsMap.get(value.roomId).name}
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
                                        {detail.id}
                                    </li>
                                ))}
                            </td>
                        </tr>

                        <tr>
                            <td>Các hình ảnh chuản đoán: </td>
                            <td></td>
                            <td></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td>1</td>
                            <td>2</td>
                        </tr>

                        <tr>
                            <td></td>
                            <td>1</td>
                            <td>2</td>
                        </tr>
                    </tbody>

                    <button>Tạo phiếu khám bệnh</button>
                </table>
            </div>
        </div>
    )
}

const ReceiptWithAppointment = ({workingScheduleMap, workingRoomsMap}) => {
    const [appointments, setAppointments] = useState([])
    const [patientsMap, setPatientsMap] = useState(new Map())
    const [isCreateModalOpen, setIsCreateModelOpen] = useState(false);
    const [selectedAppointment, setSelectedAppointment] = useState({})

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


    useEffect(() => {
        getAppointmentByToday()
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

            <CreateExaminationFormModal isOpen={isCreateModalOpen} onClose={closeCreateModal} appointment={selectedAppointment} roomsMap={workingRoomsMap} workingSchedule={workingScheduleMap} />
        </div>
    )
}

export {ReceiptWithAppointment}