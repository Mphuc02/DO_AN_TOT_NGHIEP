import {useEffect, useState} from "react";
import {GetTodayString} from "../../../service/TimeService";
import {SendApiService} from "../../../service/SendApiService";
import {ExaminationResult, PATIENT} from "../../../ApiConstant";

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

    const getAppointment = () => {
        SendApiService.getRequest(PATIENT.APPOINTMENT.getById(examinationResult.id), {}, response => {
            if(response.data){
                setAppointmentFormResponse(response.data)
            }
        }, error => {

        })
    }

    useEffect(() => {
        getAppointment()
    }, [examinationResult]);

    return (<>
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
                <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Ngày đặt lịch(tháng-ngày-năm):
                </td>
                <td>
                    {!appointmentFormResponse && <input
                        min={today}
                        onChange={(e) => {
                            setAppointmentForm({...appointmentForm, appointmentDate: e.target.value})
                        }}
                        type={"date"}/>}
                    {appointmentFormResponse && <p>{appointmentFormResponse.appointmentDate}</p>}
                </td>
            </tr>

            <tr>
                <td></td>
                <td>{errorResponse.description}</td>
            </tr>
            <tr>
                <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Lý do:</td>
                <td>
                    {!appointmentFormResponse && <input
                        className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                        onChange={(e) => {
                            setAppointmentForm({...appointmentForm, description: e.target.value})
                        }}/>}
                    {appointmentFormResponse && <p>{appointmentFormResponse.description}</p>}
                </td>
            </tr>
            </tbody>
        </table>

        {!appointmentFormResponse &&
            <button
                className="ml-10 bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                onClick={() => onClickCreateAppointmentForm()}>Đặt lịch hẹn
            </button>
        }
    </>)
}

export {AppointmentForm}