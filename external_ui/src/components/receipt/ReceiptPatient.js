import {useEffect, useState} from "react";
import styles from '../../layouts/body/style.module.css'
import {ReceiptWithAppointment} from "./ReceiptWithAppointment";
import {SendApiService} from "../../service/SendApiService";
import {EMPLOYYEE, HOSPITAL_INFORMATION, WORKING_SCHEDULE} from "../../ApiConstant";
import {ReceivedPatients} from "./ReceivedPatients";
import {GetTodayString} from "../../service/TimeService";
import {ReceiptWithFirstTimePatient} from "./ReceiptWithFirstTimePatient";

function ReceiptPatient(){
    const [selectedTab, setSelectedTab] = useState(1)
    const [workingScheduleMap, setWorkingScheduleMap] = useState(new Map())
    const [workingRoomsMap, setWorkingRoomsMap] = useState(new Map())

    const getEmployeeByIds = (doctorIdsMap) => {
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(EMPLOYYEE.findByIds(), [...doctorIdsMap.keys()], {}, response => {
                for(const doctor of response.data){
                    doctorIdsMap.get(doctor.id).doctor = doctor
                }

                console.log('doctor', doctorIdsMap)
                resolve()
            }, error => {
                reject()
            })
        })
    }

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
        // const todayTime =  new Date().toISOString().split("T")[0]
        const todayTime = GetTodayString()
        SendApiService.getRequest(WORKING_SCHEDULE.getSchedulesByDate(todayTime), {},  async (response) => {
            const tempMap = new Map()
            const doctorIdsMap = new Map()
            for(const schedule of response.data){
                tempMap.set(schedule.roomId, schedule)
                doctorIdsMap.set(schedule.employeeId, schedule)
            }
            await getEmployeeByIds(doctorIdsMap)
            setWorkingScheduleMap(tempMap)

            console.log('working' ,tempMap)
        }, (error) => {

        })
    }

    useEffect(() => {
        getWorkingScheduleByToday()
        getAllExaminationRoom()
    }, [])

    return (
        <div>
            <h2 className="text-xl font-bold text-green-600 mb-4">Tiếp đón bệnh nhân</h2>
            <div className={styles.divFlex}>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 1 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(1)}>Bệnh nhân lần đầu khám</div>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 2 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(2)}>Bệnh nhân không có lịch hẹn</div>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 3 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(3)}>Bệnh nhân có lịch hẹn</div>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 4 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(4)}>Danh sách bệnh nhân tiếp đón hôm nay</div>
            </div>

            {selectedTab === 1 && <ReceiptWithFirstTimePatient workingScheduleMap = {workingScheduleMap} workingRoomsMap = {workingRoomsMap} />}
            {selectedTab === 3 && <ReceiptWithAppointment workingScheduleMap = {workingScheduleMap} workingRoomsMap = {workingRoomsMap} />}
            {selectedTab === 4 && <ReceivedPatients />}
        </div>
    )
}

export default ReceiptPatient