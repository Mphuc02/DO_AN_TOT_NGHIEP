import {useEffect, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {EMPLOYYEE, ExaminationResult} from "../../ApiConstant";

const ExaminationResultHistories = () => {
    const [histories, setHistories] = useState([])
    const [doctorsMap, setDoctorsMap] = useState(new Map())

    const getDoctors = (histories, doctorIdsSet) => {
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(EMPLOYYEE.findByIds(), [...doctorIdsSet],{}, response => {
                const tempDoctorsMap = new Map()
                for(const doctor of response.data){
                    tempDoctorsMap.set(doctor.id, doctor)
                }
                for(const history of histories){
                    history.doctor = tempDoctorsMap.get(history.employeeId)
                }
                setDoctorsMap(tempDoctorsMap)
                resolve()
            }, error => {
                reject()
            })
        })
    }

    const getHistories = () => {
        SendApiService.getRequest(ExaminationResult.ExaminationResultUrl.findHistoriesOfPatient(), {},  async response => {
            const doctorIdSet = new Set()
            const tempHistories = response.data
            for(const result of response.data){
                doctorIdSet.add(result.employeeId)
            }

            await getDoctors(tempHistories, doctorIdSet)
            setHistories(tempHistories)
        }, error => {

        })
    }

    useEffect(() => {
        getHistories()
    }, []);

    return (<div>

    </div>)
}

export {ExaminationResultHistories}