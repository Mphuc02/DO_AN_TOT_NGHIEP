import {useEffect, useState} from "react";
import styles from '../../layouts/body/style.module.css'
import {ReceiptWithAppointment} from "./ReceiptWithAppointment";
import {SendApiService} from "../../service/SendApiService";
import {HOSPITAL_INFORMATION, WORKING_SCHEDULE} from "../../ApiConstant";

function ReceiptPatient(){
    const [selectedTab, setSelectedTab] = useState(1)
    const [workingScheduleMap, setWorkingScheduleMap] = useState(new Map())
    const [workingRoomsMap, setWorkingRoomsMap] = useState(new Map())

    const getAllExaminationRoom = async () => {
        SendApiService.getRequest(HOSPITAL_INFORMATION.EXAMINATION_ROOM.getUrl(), {}, (response) => {
            const tempMap = new Map()
            for(const room of response.data){
                tempMap.set(room.id, room)
            }
            setWorkingRoomsMap(tempMap)
        }, (error) => {

        })
    }

    const getWorkingScheduleByToday = async() => {
        const todayTime = new Date().toISOString().split("T")[0]
        SendApiService.getRequest(WORKING_SCHEDULE.getSchedulesByDate(todayTime), {}, (response) => {
            const tempMap = new Map()
            for(const schedule of response.data){
                tempMap.set(schedule.roomId, schedule)
            }
            setWorkingScheduleMap(tempMap)
        }, (error) => {

        })
    }

    useEffect(() => {
        getWorkingScheduleByToday()
        getAllExaminationRoom()
    }, [])

    return (
        <div>
            <h2>Tiếp đón bệnh nhân</h2>
            <div className={styles.divFlex}>
                <div onClick={() => setSelectedTab(1)}>Bệnh nhân lần đầu khám</div>
                <div onClick={() => setSelectedTab(2)}>Bệnh nhân không có lịch hẹn</div>
                <div onClick={() => setSelectedTab(3)}>Bệnh nhân có lịch hẹn</div>
            </div>

            {selectedTab === 3 && <ReceiptWithAppointment workingScheduleMap = {workingScheduleMap} workingRoomsMap = {workingRoomsMap} />}
        </div>
    )
}

export default ReceiptPatient