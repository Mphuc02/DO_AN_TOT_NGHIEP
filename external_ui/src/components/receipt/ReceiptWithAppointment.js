import {useState, useEffect} from "react";
import {SendApiService} from "../../service/SendApiService";
import {PATIENT} from "../../ApiConstant";

const ReceiptWithAppointment = () => {
    const [appointments, setAppointments] = useState([])
    const getAppointmentByToday = () => {
        SendApiService.getRequest(PATIENT.APPOINTMENT.getAppointmentsOfToday(), {}, (response) => {
            setAppointments(response.data)
        }, (error) => {

        })
    }

    useEffect(() => {
        getAppointmentByToday()
    })

    return (
        <div>
            <h2>Tiếp đón với lịch hẹn</h2>
            <table>
                <tbody>

                </tbody>
            </table>
        </div>
    )
}

export {ReceiptWithAppointment}