import {useEffect} from "react";
import {SendApiService} from "../../service/SendApiService";
import {Greeting} from "../../ApiConstant";

const ReceivedPatients = () => {
    const getReceivedPatientsToday = () => {
        SendApiService.getRequest(Greeting.ExaminationForm.findReceivedPatientsToday(), {}, (response) => {

        }, (error) => {

        })
    }

    useEffect(() => {
        getReceivedPatientsToday()
    }, [])

    return (
        <div>

        </div>
    )
}

export {ReceivedPatients}