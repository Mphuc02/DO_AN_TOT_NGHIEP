import styles from "../../layouts/body/style.module.css";
import {useEffect, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {EMPLOYYEE, HOSPITAL_INFORMATION, PATIENT, WORKING_SCHEDULE} from "../../ApiConstant";
import ToastPopup from "../common/ToastPopup";
import {FormatDate} from "../../service/TimeService";

const daysOfWeek = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];

const UpdateModal = ({isOpen, onClose, data, roomMap}) => {
    const [workingScheduleMap, setWorkingScheduleMap] = useState(new Map())
    const [doctorsMap, setDoctorsMap] = useState(new Map())

    const getEmployeeByIds = (doctorIdsMap) => {
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(EMPLOYYEE.findByIds(), [...doctorIdsMap.keys()], {}, response => {
                const tempMap = new Map()
                for(const doctor of response.data){
                    doctorIdsMap.get(doctor.id).doctor = doctor
                    tempMap.set(doctor.id, doctor)
                }
                setDoctorsMap(tempMap)

                resolve()
            }, error => {
                reject()
            })
        })
    }

    const getWorkingScheduleByDate = async() => {
        // const todayTime =  new Date().toISOString().split("T")[0]
        SendApiService.getRequest(WORKING_SCHEDULE.getSchedulesByDate(updatingData.appointmentDate), {},  async (response) => {
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

    const [updatingData, setUpdatingData] = useState({
        ...data
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
        getWorkingScheduleByDate()
    }, [updatingData.appointmentDate]);

    useEffect(() => {
        if (data) {
            setUpdatingData({...data});
        }
        setErrorResponse({})
    }, [data]);

    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const formattedTomorrow = tomorrow.toISOString().split('T')[0];

    if (!isOpen) {
        return null;
    }

    const handleUpdateSchedule = async () => {
        SendApiService.putRequest(PATIENT.APPOINTMENT.getById(updatingData.id), updatingData, {}, response => {
            setToast({message: "Cập nhật lịch hẹn khám thành công", type: 'ok'})
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
        SendApiService.deleteRequest(PATIENT.APPOINTMENT.getById(id), {}, {}, response => {
            setToast({message: 'Xóa lịch hẹn khám thành công', type: 'ok'})
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
                    onClick={onClose}>
                    &times;
                </button>

                <h1 className="text-2xl font-semibold text-green-600 mb-4 text-center">
                    Cập nhật thông tin lịch khám
                </h1>

                <table className="w-full">
                    {(() => {
                        const today = new Date();
                        const appointmentDate = new Date(data.appointmentDate);
                        const doctor = doctorsMap.get(data.doctorId)
                        if(!doctor){
                            return null
                        }
                        const fullName = doctor.fullName
                        const fullNameStr = fullName.lastName + ' ' + fullName.middleName + ' ' + fullName.firstName

                        if (today >= appointmentDate) {
                            return (
                                <tbody>
                                <tr className="flex mb-2">
                                    <td className="font-semibold w-1/3">Bác sĩ:</td>
                                    <td>{fullNameStr}</td>
                                </tr>

                                <tr className="flex mb-2">
                                    <td className="font-semibold w-1/3">Ngày khám:</td>
                                    <td>{updatingData.appointmentDate.split('-').reverse().join('-')}</td>
                                </tr>

                                <tr className="flex mb-2">
                                    <td className="font-semibold w-1/3">Ghi chú:</td>
                                    <td className=" w-1/3">{data.description}</td>
                                </tr>
                                </tbody>
                            );
                        }
                        return (
                            <tbody>
                            <tr>
                                <td></td>
                                <td><p className="text-red-500 text-sm font-medium"> {errorResponse.doctorId}</p></td>
                            </tr>

                            <tr>
                                <td className="font-semibold">Bác sĩ</td>
                                <td>
                                    <select
                                        className="border border-gray-300 rounded-lg px-3 py-2 w-full focus:ring-2 focus:ring-green-500"
                                        value={data.doctorId}
                                        onChange={(e) => setUpdatingData({...updatingData, doctorId: e.target.value})}>
                                        <option value="">Chọn bác sĩ khám bệnh</option>
                                        {[...workingScheduleMap].map(([key, value]) => {
                                            const fullName = value.doctor.fullName
                                            if (!fullName) {
                                                return null
                                            }
                                            const fullNameStr = fullName.lastName + ' ' + fullName.middleName + ' ' + fullName.firstName
                                            return <option key={key} value={value.employeeId}>
                                                {fullNameStr}
                                            </option>
                                        })}
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <td></td>
                                <td><p className="text-red-500 text-sm font-medium">{errorResponse.appointmentDate}</p>
                                </td>
                            </tr>
                            <tr>
                                <td className="font-semibold">Ngày khám(ngày/tháng/năm):</td>
                                <td className="border p-2 rounded-md w-full">{FormatDate(data.appointmentDate)}</td>
                            </tr>

                            <tr>
                                <td>Ghi chú:</td>
                                <td><input
                                    className="w-full rounded-md p-2 mb-2 mt-2"
                                    value={data.description}
                                    onChange={(e) => setUpdatingData({...updatingData, description: e.target.value})}/>
                                </td>
                            </tr>

                            <tr className="mt-4">
                                <td>
                                    <button
                                        className="bg-green-500 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-green-600 focus:outline-none"
                                        onClick={() => handleUpdateSchedule()}>
                                        Lưu
                                    </button>
                                </td>
                                <td>
                                    <button
                                        className="bg-red-500 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-red-600 focus:outline-none"
                                        onClick={() => handleDeleteSchedule(updatingData.id)}>
                                        Xóa lịch hẹn khám
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

const CreateModal = ({isOpen, onClose, roomMap, date}) => {
    const [workingScheduleMap, setWorkingScheduleMap] = useState(new Map())

    const getEmployeeByIds = (doctorIdsMap) => {
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(EMPLOYYEE.findByIds(), [...doctorIdsMap.keys()], {}, response => {
                for(const doctor of response.data){
                    doctorIdsMap.get(doctor.id).doctor = doctor
                }

                resolve()
            }, error => {
                reject()
            })
        })
    }

    const getWorkingScheduleByDate = async() => {
        // const todayTime =  new Date().toISOString().split("T")[0]
        SendApiService.getRequest(WORKING_SCHEDULE.getSchedulesByDate(date), {},  async (response) => {
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

    const [creatingData, setCreatingData] = useState({
        doctorId: '',
        appointmentDate: date
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
        getWorkingScheduleByDate()
        if (date) {
            setCreatingData({...creatingData, appointmentDate: date});
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

    const handleCreateAppointment = async () => {
        SendApiService.postRequest(PATIENT.APPOINTMENT.getUrl(), creatingData, {}, response => {
            setToast({message: "Đăng ký lịch hẹn khám thành công", type: 'oke'})
            setTimeout(() => {
                window.location.reload();
            }, 1000)
        }, error => {
            if(error.status === 400){
                setErrorResponse(error.response.data.fields)
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
                    Đăng ký lịch hẹn khám
                </h1>

                {/* Nội dung */}
                <table className="w-full mb-6 text-left">
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{errorResponse.doctorId}</p></td>
                    </tr>
                    <tr>
                        <td className="pr-4 text-gray-700">Bác sĩ:</td>
                        <td>
                            <select
                                className="border border-gray-300 rounded-lg px-3 py-2 w-full focus:ring-2 focus:ring-green-500"
                                onChange={(e) => setCreatingData({...creatingData, doctorId: e.target.value})}>
                                <option value="">Chọn bác sĩ khám bệnh</option>
                                {[...workingScheduleMap].map(([key, value]) => {
                                    const fullName = value.doctor.fullName
                                    if (!fullName) {
                                        return null
                                    }
                                    const fullNameStr = fullName.lastName + ' ' + fullName.middleName + ' ' + fullName.firstName
                                    return <option key={key} value={value.employeeId}>
                                        {fullNameStr}
                                    </option>
                                })}
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td className="pr-4 text-gray-700">Ngày khám:</td>
                        <td>
                            <input
                                type="date"
                                min={formattedTomorrow}
                                value={creatingData.appointmentDate}
                                onChange={(e) => setCreatingData({...creatingData, appointmentDate: e.target.value})}
                                className="border border-gray-300 rounded-lg px-3 py-2 w-full focus:ring-2 focus:ring-green-500 mt-5 mb-5"
                            />
                        </td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{errorResponse.description}</p></td>
                    </tr>

                    <tr>
                        <td>Ghi chú:</td>
                        <td><input
                            className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                            onChange={(e) => setCreatingData({...creatingData, description: e.target.value})}/></td>
                    </tr>
                    </tbody>
                </table>

                {/* Nút lưu */}
                <div className="flex justify-center">
                    <button
                        onClick={handleCreateAppointment}
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
    const [appointments, setAppointments] = useState([])
    const getAppointments = () => {
        SendApiService.getRequest(PATIENT.APPOINTMENT.getAppointmentsOfPatient(currentYear, currentMonth + 1), {}, response => {
            setAppointments(response.data)
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
                                        if (appointments[index] != null && dayTemp === appointments[index].appointmentDate) {
                                            const appointment = appointments[index]
                                            return <div onClick={() => handleUpdateSchedule(appointment)}
                                                        className={`${styles.square} ${!appointments[index++]?.isExamined ? 'bg-green-500' : 'bg-blue-500'}`}>
                                                <div>{day ? day : ''}</div>
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

                <div className={"flex"}>
                    <div className={`${styles.square} mr-10 bg-green-500`}>Lịch hẹn chưa khám</div>
                    <div className={`${styles.square} bg-blue-500`}>Lịch hẹn đã khám khám</div>
                </div>

                <UpdateModal isOpen={isUpdateModalOpen} onClose={closeUpdateModal} data={updatingData}
                             roomMap={roomMap}>
                </UpdateModal>

                <CreateModal isOpen={isCreateModalOpen} onClose={closeCreateModal} roomMap={roomMap}
                             date={creatingDate}/>
            </div>
        </div>)
}

export {AppointmentManagement}