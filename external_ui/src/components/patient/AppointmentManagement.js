import styles from "../../layouts/body/style.module.css";
import {useEffect, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {HOSPITAL_INFORMATION, WORKING_SCHEDULE} from "../../ApiConstant";

const daysOfWeek = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];

const AppointmentManagement = () => {
    const [roomMap, setRoomMap] = useState(new Map())
    const [currentDate, setCurrentDate] = useState(new Date());

    const currentMonth = currentDate.getMonth();
    const currentYear = currentDate.getFullYear();

    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    const firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();
    const days = Array.from({length: daysInMonth}, (_, index) => index + 1);

    const handlePreviousMonth = () => {
        setCurrentDate(new Date(currentYear, currentMonth - 1, 1));
    };

    //Click next month
    const handleNextMonth = () => {
        setCurrentDate(new Date(currentYear, currentMonth + 1, 1));
    };

    const calendarDays = [];
    let week = Array(firstDayOfMonth).fill(null);

    //Get all room

    const getAllRooms = () => {
        SendApiService.getRequest(HOSPITAL_INFORMATION.EXAMINATION_ROOM.getUrl(), {}, response => {
            const map = new Map()
            response.data.forEach(item => map.set(item.id, item))
            setRoomMap(map)
        }, error => {

        })
    }

    //Get all Schedule
    const [schedules, setSchedules] = useState([])
    const getAppointments = () => {
        SendApiService.getRequest(WORKING_SCHEDULE.getSchedulesInMonthOfEmployee(currentYear, currentMonth + 1), {}, response => {
            setSchedules(response.data)
        }, error => {

        })
    }

    useEffect(() => {
        getAppointments()
        getAllRooms()
    }, [currentDate])

    days.forEach((day) => {
        week.push(day);
        if (week.length === 7) {
            calendarDays.push(week);
            week = [];
        }
    });


    if (week.length > 0) {
        calendarDays.push(week.concat(Array(7 - week.length).fill(null))); // Thêm các ô trống vào cuối tuần nếu chưa đủ 7 ngày
    }

    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [creatingDate, setCreatingDate] = useState('')
    const openCreateModal = () => {
        setIsCreateModalOpen(true);
    };

    const closeCreateModal = () => {
        setIsCreateModalOpen(false);
    };
    const handleCreateSchedule = (dayTemp) => {
        setCreatingDate(dayTemp)
        openCreateModal()
    }

    const [updatingData, setUpdatingData] = useState({
        id: '',
        roomId: '',
        date: ''
    })
    const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
    const openUpdateModal = () => {
        setIsUpdateModalOpen(true);
    };

    const closeUpdateModal = () => {
        setUpdatingData({id: '', roomId: '', date: ''})
        setIsUpdateModalOpen(false);
    };
    const handleUpdateSchedule = (schedule) => {
        setUpdatingData(schedule)
        openUpdateModal()
    }

    let index = 0

    return (
        <div className={"mt-10"}>
            <h2 className="text-xl font-bold text-green-600 mb-4">Quản lý lịch hẹn khám</h2>

            <div>
                <div className={styles.header}>
                    <button
                        className="bg-green-500 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-green-600 active:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-300 disabled:bg-green-200"
                        onClick={handlePreviousMonth}>Tháng trước
                    </button>
                    <span>Tháng {currentMonth + 1} / {currentYear}</span>
                    <button
                        className="bg-green-500 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-green-600 active:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-300 disabled:bg-green-200"
                        onClick={handleNextMonth}>Tháng sau
                    </button>
                </div>

                <div className={styles.weekdays}>
                    {daysOfWeek.map((day, index) => (
                        <div key={index} className={styles.weekday}>
                            {day}
                        </div>
                    ))}
                </div>

                <div className={styles.calendar}>
                    {calendarDays.map((week, weekIndex) => (
                        <div key={weekIndex} className={styles.week}>
                            {week.map((day, dayIndex) => (
                                <div key={dayIndex}>
                                    <div>{(() => {
                                        const tomorrow = new Date()
                                        tomorrow.setDate(tomorrow.getDate() + 1)
                                        tomorrow.setUTCHours(0, 0, 0, 0)
                                        const dayTemp = currentYear + '-' + ((currentMonth + 1) < 10 ? '0' + (currentMonth + 1) : (currentMonth + 1)) + '-' + (day < 10 ? '0' + day : day)
                                        console.log(dayTemp)
                                        if (schedules[index] != null && dayTemp === schedules[index].date) {
                                            const schedule = schedules[index]
                                            index++
                                            return <div onClick={() => handleUpdateSchedule(schedule)}
                                                        className={`${styles.square} ${styles.cursorPointer}`}>
                                                <div>{day + " (" + (roomMap.get(schedule.roomId) ? roomMap.get(schedule.roomId).name : '') + ")"}</div>
                                            </div>
                                        } else if (tomorrow <= new Date(dayTemp)) {
                                            return <div onClick={() => handleCreateSchedule(dayTemp)}
                                                        className={`${styles.square} ${styles.cursorPointer}`}> {day ? day : ''}</div>
                                        } else if (tomorrow > new Date(dayTemp)) {
                                            return <div className={styles.square}> {day ? day : ''}</div>
                                        }
                                    })()}</div>
                                </div>
                            ))}
                        </div>
                    ))}
                </div>

                {/*<UpdateModal isOpen={isUpdateModalOpen} onClose={closeUpdateModal} data={updatingData}*/}
                {/*             roomMap={roomMap}>*/}
                {/*</UpdateModal>*/}

                {/*<CreateModal isOpen={isCreateModalOpen} onClose={closeCreateModal} roomMap={roomMap}*/}
                {/*             date={creatingDate}/>*/}
            </div>
        </div>)
}

export {AppointmentManagement}