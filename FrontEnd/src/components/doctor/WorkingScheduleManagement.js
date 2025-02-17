import {useState, useEffect, useRef } from "react";
import {EMPLOYYEE, HOSPITAL_INFORMATION, WORKING_SCHEDULE} from "../../ApiConstant";
import axios from "axios";
import {JwtService} from "../../service/JwtService";
import styles from '../../layouts/body/style.module.css'
import ToastPopup from "../common/ToastPopup";
import {SendApiService} from "../../service/SendApiService";


const daysOfWeek = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];
let page
let countError = 0

const CreateModal = ({ isOpen, onClose, roomMap, date }) => {
    const [creatingData, setCreatingData] = useState({
        roomId: '',
        date: date
    })
    const [toast, setToast] = useState(null)

    const hideToast = () => {
        setToast(null);
    };

    const [errorResponse, setErrorResponse] = useState({
        roomId: '',
        date: ''
    })

    useEffect(() => {
        if (date) {
            setCreatingData({...creatingData, date: date});
        }
        setErrorResponse({})
    }, [date]);
    console.log(creatingData)

    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const formattedTomorrow = tomorrow.toISOString().split('T')[0];

    if (!isOpen) {
        return null;
    }

    const handleCreateSchedule = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.post(WORKING_SCHEDULE.getUrl(), JSON.stringify(creatingData), {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            console.log(response)
            countError = 0
            setToast({message: "Thêm thông tin ngày làm việc thành công", type: 'oke'})
            setTimeout(() => {
                window.location.reload();
            }, 1000)
        }).catch(async (error) => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await handleCreateSchedule()
            }
            if(error.status === 400){
                setErrorResponse(error.response.data.fields)
                setToast({message: "Lỗi khi kiểm tra thông tin", type: 'error'})
            }
        })
    }

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div className="bg-white rounded-lg shadow-lg p-6 max-w-lg w-full relative">
                {/* Nút đóng */}
                <button
                    onClick={onClose}
                    className="text-red-600 hover:text-red-800 absolute top-4 right-4 text-3xl font-bold">
                    &times;
                </button>

                {/* Tiêu đề */}
                <h1 className="text-green-600 text-2xl font-semibold mb-4 text-center">
                    Đăng ký lịch làm việc
                </h1>

                {/* Nội dung */}
                <table className="w-full mb-6 text-left">
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{errorResponse.roomId}</p></td>
                    </tr>
                    <tr>
                    <td className="pr-4 text-gray-700">Phòng khám:</td>
                        <td>
                            <select
                                className="border border-gray-300 rounded-lg px-3 py-2 w-full focus:ring-2 focus:ring-green-500"
                                onChange={(e) => setCreatingData({...creatingData, roomId: e.target.value})}>
                                <option value="">Chọn phòng làm việc</option>
                                {[...roomMap].map(([key, value]) => (
                                    <option key={key} value={key}>
                                        {value.name}
                                    </option>
                                ))}
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td className="pr-4 text-gray-700">Ngày khám:</td>
                        <td>
                            <input
                                type="date"
                                min={formattedTomorrow}
                                value={creatingData.date}
                                onChange={(e) => setCreatingData({...creatingData, date: e.target.value})}
                                className="border border-gray-300 rounded-lg px-3 py-2 w-full focus:ring-2 focus:ring-green-500"
                            />
                        </td>
                    </tr>
                    </tbody>
                </table>

                {/* Nút lưu */}
                <div className="flex justify-center">
                    <button
                        onClick={handleCreateSchedule}
                        className="bg-green-500 text-white font-semibold px-6 py-2 rounded-lg shadow-md hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400">
                        Lưu
                    </button>
                </div>
            </div>
                {toast && <ToastPopup message={toast.message}
                                      type={toast.type}
                                      onClose={hideToast}/>}
        </div>
    );
}

const UpdateModal = ({isOpen, onClose, data, roomMap}) => {
    const [updatingData, setUpdatingData] = useState({...data});
    const [errorResponse, setErrorResponse] = useState({
        roomId: '',
        date: ''
    })
    const [toast, setToast] = useState(null)

    const hideToast = () => {
        setToast(null)
    }

    useEffect(() => {
        if (data) {
            setUpdatingData({...data});
            setErrorResponse({
                roomId: '',
                date: ''
            })
        }
    }, [data]);

    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const formattedTomorrow = tomorrow.toISOString().split('T')[0];

    if (!isOpen) {
        return null;
    }

    const handleUpdateSchedule = async () => {
        if (countError === 5)
            return

        SendApiService.putRequest(WORKING_SCHEDULE.getUrlById(updatingData.id), updatingData, {}, response => {
            setToast({message: "Cập nhật lịch khám thành công", type: 'ok'})
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        }, error => {
            if(error.status === 400){
                setErrorResponse(error.response.data.fields)
                setToast({message: 'Lỗi khi kiểm tra thông tin', type: 'error'})
            }
        })
    }

    const handleDeleteSchedule = async (id) => {
        SendApiService.deleteRequest(WORKING_SCHEDULE.getUrlById(id), {}, {}, response => {
            setToast({message: 'Xóa lịch làm việc thành công', type: 'ok'})
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        }, error => {

        })
    }

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-full sm:w-96 relative">
                {/* Nút tắt modal */}
                <button
                    className="absolute top-2 right-2 text-3xl text-red-600 hover:text-red-800"
                    onClick={onClose}
                >
                    &times;
                </button>

                <h1 className="text-2xl font-semibold text-green-600 mb-4 text-center">
                    Cập nhật thông tin lịch khám
                </h1>

                <table className="w-full">
                    {(() => {
                        const today = new Date();
                        const scheduleDate = new Date(updatingData.date);
                        if (today >= scheduleDate) {
                            return (
                                <tbody>
                                <tr className="flex mb-2">
                                    <td className="font-semibold w-1/3">Phòng khám:</td>
                                    <td>{roomMap.get(updatingData.roomId).name}</td>
                                </tr>

                                <tr className="flex mb-2">
                                    <td className="font-semibold w-1/3">Ngày khám:</td>
                                    <td>{updatingData.date.split('-').reverse().join('-')}</td>
                                </tr>
                                </tbody>
                            );
                        }
                        return (
                            <tbody>
                            <tr>
                                <td></td>
                                <td><p className="text-red-500 text-sm font-medium"> {errorResponse.roomId}</p></td>
                            </tr>

                            <tr>
                                <td className="font-semibold">Phòng khám</td>
                                <td>
                                    <select
                                        className="border p-2 rounded-md w-full"
                                        value={updatingData.roomId}
                                        onChange={(e) =>
                                            setUpdatingData({
                                                ...updatingData,
                                                roomId: e.target.value,
                                            })
                                        }
                                    >
                                        {[...roomMap].map(([key, value]) => (
                                            <option key={key} value={key}>
                                                {value.name}
                                            </option>
                                        ))}
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <td></td>
                                <td><p className="text-red-500 text-sm font-medium">{errorResponse.date}</p></td>
                            </tr>
                            <tr>
                                <td className="font-semibold">Ngày khám</td>
                                <td>
                                    <input
                                        className="border p-2 rounded-md w-full"
                                        type="date"
                                        value={updatingData.date}
                                        onChange={(e) =>
                                            setUpdatingData({
                                                ...updatingData,
                                                date: e.target.value,
                                            })
                                        }
                                        min={formattedTomorrow}
                                    />
                                </td>
                            </tr>

                            <tr className="mt-4">
                                <td>
                                    <button
                                        className="bg-green-500 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-green-600 focus:outline-none"
                                        onClick={() => handleUpdateSchedule()}
                                    >
                                        Lưu
                                    </button>
                                </td>
                                <td>
                                    <button
                                        className="bg-red-500 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-red-600 focus:outline-none"
                                        onClick={() => handleDeleteSchedule(updatingData.id)}
                                    >
                                        Xóa lịch làm việc
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        );
                    })()}
                </table>
            </div>

            {toast && <ToastPopup message={toast.message} type={toast.type} onClose={hideToast}/>}
        </div>
    );
};

const WorkingScheduleInMonth = () => {
    page = 'list'

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
    const getSchedules = () => {
        SendApiService.getRequest(WORKING_SCHEDULE.getSchedulesInMonthOfEmployee(currentYear, currentMonth + 1), {}, response => {
            setSchedules(response.data)
        }, error => {

        })
    }

    useEffect(() => {
        getSchedules()
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
        <div>
            <div className={styles.header}>
                <button
                    className="bg-green-500 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-green-600 active:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-300 disabled:bg-green-200"
                    onClick={handlePreviousMonth}>Tháng trước
                </button>
                <span>
          Tháng {currentMonth + 1} / {currentYear}
        </span>
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
                                    tomorrow.setUTCHours(0,0,0,0)
                                    const dayTemp = currentYear + '-' + ( (currentMonth+1) < 10 ? '0' + (currentMonth+1) : (currentMonth+1)) + '-' + (day < 10 ? '0' + day : day)
                                    console.log(dayTemp)
                                    if(schedules[index] != null && dayTemp === schedules[index].date){
                                        const schedule = schedules[index]
                                        index++
                                        return <div onClick={() => handleUpdateSchedule(schedule)} className={`${styles.square} ${styles.cursorPointer}`}><div>{day + " (" + (roomMap.get(schedule.roomId) ? roomMap.get(schedule.roomId).name : '') + ")"}</div></div>
                                    }
                                    else if(tomorrow <= new Date(dayTemp)){
                                        return <div onClick={() =>  handleCreateSchedule(dayTemp)} className={`${styles.square} ${styles.cursorPointer}`}> {day ? day : ''}</div>
                                    }
                                    else if(tomorrow > new Date(dayTemp)){
                                        return <div className={styles.square}> {day ? day : ''}</div>
                                    }
                                })()}</div>
                            </div>
                        ))}
                    </div>
                ))}
            </div>

            <UpdateModal isOpen={isUpdateModalOpen} onClose={closeUpdateModal} data={updatingData} roomMap={roomMap}>
            </UpdateModal>

            <CreateModal isOpen={isCreateModalOpen} onClose={closeCreateModal} roomMap={roomMap} date={creatingDate}/>
        </div>
    );
};

function WorkingScheduleManagement(){
    page = 'list'
    const [data, setData] = useState([]);
    const [error, setError] = useState(null);
    let countErrorTime = 0;

    const getEmployees = async () => {
        if(countErrorTime === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(EMPLOYYEE.getUrl(""), {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        })
            .then(response => {
                setData(response.data);
            })
            .catch(async error => {
                setError('Error fetching data');
                const result = await JwtService.checkTokenExpired(error)
                if(result){
                    await getEmployees()
                }
            });
    }

    useEffect(() => {
        getEmployees()
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h2 className="text-xl font-bold text-green-600 mb-4">Quản lý lịch làm việc</h2>
            <WorkingScheduleInMonth/>
        </div>
    );
}

export {WorkingScheduleManagement}