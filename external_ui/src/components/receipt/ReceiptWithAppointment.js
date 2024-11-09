import {useState, useEffect, useRef} from "react";
import {SendApiService} from "../../service/SendApiService";
import {Greeting, HOSPITAL_INFORMATION, PATIENT, WEBSOCKET} from "../../ApiConstant";
import styles from '../../layouts/body/style.module.css'
import WebSocketService from "../../service/WebSocketService";
import {JwtService} from "../../service/JwtService";
import { jsPDF } from 'jspdf';

//Todo: Kiểm tra dữ liệu có hợp lệ hay không

const printGreetingForm = (form, appointment) => {
    const ticketData = {
        hospitalName: "Benh vien da lieu Minh Phuc",
        ...form,
        ...appointment
    };

    console.log(ticketData)

    const createAndPrintPDF = () => {
        const doc = new jsPDF();

        // Nội dung PDF
        doc.setFontSize(20);
        doc.text(ticketData.hospitalName, 105, 20, { align: 'center' });

        doc.setFontSize(16);
        doc.text('PHIEU SO THU TU DOI KHAM', 105, 40, { align: 'center' });

        doc.setFontSize(30);
        doc.text(`So Thu Tu: ${ticketData.examinedNumber}`, 105, 70, { align: 'center' });

        doc.setFontSize(14);
        doc.text(`Ten Benh Nhan: ${ticketData.fullName.firstName + " " + ticketData.fullName.middleName + " " + ticketData.fullName.lastName}`, 20, 100);
        doc.text(`Phong Kham: ${ticketData.roomName}`, 20, 120);

        doc.setFontSize(12);
        doc.text('Xin vui long doi den khi so cua ban duoc goi de vao kham.', 105, 140, { align: 'center' });

        // Mở hộp thoại in
        doc.autoPrint();
        window.open(doc.output('bloburl'), '_blank');
    };

    createAndPrintPDF()
}

const CreateExaminationFormModal = ({ isOpen, onClose, appointment, roomsMap, workingSchedule, diseasesMap, appointmentsMap, createdExaminationForm }) => {
    const [createExaminationForm, setCreateExaminationForm] = useState({...appointment, symptom: appointment.description})
    const [createExaminationFormError, setCreateExaminationFormError] = useState({})
    let sendingApi = false

    useEffect(() => {
        const temp = {...appointment, symptom: appointment.description}
        for(const [key, value] of workingSchedule){
            if(value.employeeId === appointment.doctorId){
                temp.workingScheduleId = value.id
            }
        }
        setCreateExaminationForm(temp)
    },[appointment])

    useEffect(() => {

    }, [appointment.id]);

    if(!isOpen){
        return null
    }

    const handleCreateExaminationForm = () => {
        if(sendingApi)
            return

        sendingApi = true
        //Todo: Sau bỏ fix cứng số thứ tự
        createExaminationForm.appointmentId = appointment.id
        createExaminationForm.numberCall = 1
        SendApiService.postRequest(Greeting.ExaminationForm.withAppointment(), createExaminationForm, {'Content-type': 'application/json'}, (response) => {
            appointmentsMap.delete(appointment.id)
        }, (error) => {
            if(error.status === 400){
                setCreateExaminationFormError(error.response.data.fields)
                alert(error.response.data.message)
            }
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
                        <td></td>
                        <td>{createExaminationFormError.workingScheduleId}</td>
                    </tr>

                    <tr>
                        <td>Chọn phòng khám:</td>
                        <td>
                            <select
                                value={createExaminationForm.workingScheduleId || ""}
                                onChange={(e) => setCreateExaminationForm({
                                    ...createExaminationForm,
                                    workingScheduleId: e.target.value
                                })}>
                                <option value={""}>-----------</option>
                                {[...workingSchedule].map(([key, value]) => (
                                    <option key={key} value={value.id}>
                                        {(() => {
                                            const roomName = roomsMap.get(value.roomId).name
                                            appointment.roomName = roomName
                                            return roomName
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

                    <tr>
                        <td></td>
                        <td>{createExaminationFormError.symptom}</td>
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
                {!createdExaminationForm &&
                    <button onClick={() => handleCreateExaminationForm()}>Tạo phiếu khám bệnh</button>}

                {createdExaminationForm &&
                    <button onClick={() => printGreetingForm(createdExaminationForm, appointment)}>In phiếu khám bệnh</button>}
            </div>
        </div>
    )
}

const ReceiptWithAppointment = ({workingScheduleMap, workingRoomsMap}) => {
    const [appointmentsMap, setAppointmentsMap] = useState(new Map())
    const [patientsMap, setPatientsMap] = useState(new Map())
    const [isCreateModalOpen, setIsCreateModelOpen] = useState(false);
    const [selectedAppointment, setSelectedAppointment] = useState({})
    const [diseasesMap, setDiseasesMap] = useState(new Map())
    const [createdExaminationForm, setCreatedExaminationForm] = useState(null)
    const [webSocket, setWebsocket] = useState(new WebSocketService())

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
            const tempAppointment = new Map()
            const tempPatientSet = new Set()
            for (let appointment of response.data) {
                tempPatientSet.add(appointment.patientId)
                tempAppointment.set(appointment.id, appointment)
            }
            setAppointmentsMap(tempAppointment)
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

    const receivedCreatedExaminationForm = (message) => {
        const data = JSON.parse(message)
        alert(data.message)
        console.log(data.data)
        setCreatedExaminationForm(data.data)
    }

    const connectToWebSocket = () => {
        const topics = [WEBSOCKET.updatedNumberExaminationForm(JwtService.geUserFromToken())]
        webSocket.connectAndSubscribe(topics, (message) => {
            receivedCreatedExaminationForm (message)
        })
    }

    const disconnect = () => {
        webSocket.disconnect()
    }

    useEffect(() => {
        getAppointmentByToday()
        getDiseases()
        connectToWebSocket()

        return () => {
            disconnect()
        }
    }, [])

    const handleCreateExaminationForm = (appointment) => {
        appointment.fullName = patientsMap.get(appointment.patientId).fullName
        setSelectedAppointment(appointment)
        openCreateModal()
    }

    const openCreateModal = () => {
        setCreatedExaminationForm(null)
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
                {[...appointmentsMap].map(([key, appointment]) => (
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

            <CreateExaminationFormModal  isOpen={isCreateModalOpen} onClose={closeCreateModal} appointment={selectedAppointment} roomsMap={workingRoomsMap} workingSchedule={workingScheduleMap} diseasesMap={diseasesMap} appointmentsMap={appointmentsMap} createdExaminationForm={createdExaminationForm} />
        </div>
    )
}

export {ReceiptWithAppointment}