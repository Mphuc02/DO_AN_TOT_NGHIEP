import {useState} from "react";
import {GetTodayString} from "../../../service/TimeService";
import {SendApiService} from "../../../service/SendApiService";
import {PATIENT} from "../../../ApiConstant";

const AppointmentForm = ({examinationResult}) => {
    const [appointmentForm, setAppointmentForm] = useState({patientId: examinationResult.patientId,
                                                                                                        examinationResultId: examinationResult.id})
    const [appointmentFormResponse, setAppointmentFormResponse] = useState(null)
    const [errorResponse, setErrorResponse] = useState({})
    let isClickCreate = false
    const today = GetTodayString()

    const onClickCreateAppointmentForm = () => {
        SendApiService.postRequest(PATIENT.APPOINTMENT.doctorCreateAppointment(), appointmentForm, {}, response => {
            setAppointmentFormResponse(response.data)
            setErrorResponse({})
            alert("Tạo lịch hẹn thành công")
        }, error => {
            isClickCreate = false;
            if(error.status === 400){
                if(error.response.data.message){
                    alert(error.response.data.message)
                }

                setErrorResponse(error.response.data.fields)
            }
        })
    }

    return (<>
        <h2>Tạo lịch hẹn khám lại</h2>
        <table>
            <thead>
                <tr></tr>
            </thead>
            <tbody>
                <tr>
                    <td></td>
                    <td>{errorResponse.appointmentDate}</td>
                </tr>

                <tr>
                    <td>Ngày đặt lịch(tháng-ngày-năm):</td>
                    <td> <input min={today} onChange={(e) => {setAppointmentForm({...appointmentForm, appointmentDate: e.target.value})}} type={"date"}/> </td>
                </tr>

                <tr>
                    <td></td>
                    <td>{errorResponse.description}</td>
                </tr>
                <tr>
                    <td>Lý do:</td>
                    <td> <input onChange={(e) => {setAppointmentForm({...appointmentForm, description: e.target.value})}} /> </td>
                </tr>
            </tbody>

            <button onClick={() => onClickCreateAppointmentForm()}>Đặt lịch hẹn</button>
        </table>
    </>)
}

export {AppointmentForm}