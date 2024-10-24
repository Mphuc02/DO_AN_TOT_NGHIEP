import {useState, useEffect, useRef } from "react";
import {AUTHENTICATION, EMPLOYYEE, HOSPITAL_INFORMATION, ROLE, WEBSOCKET, WORKING_SCHEDULE} from "../../ApiConstant";
import axios, {get} from "axios";
import {JwtService} from "../../service/JwtService";
import {Link} from "react-router-dom";
import WebSocketService from "../../service/WebSocketService";
import StatusBar from "../common/StatusBar";
import styles from '../../layouts/body/style.module.css'
import update from '../../imgs/update.png'


const daysOfWeek = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];
let page
let countError = 0

const CreateModal = ({ isOpen, onClose, roomMap, date }) => {
    const [creatingData, setCreatingData] = useState({
        roomId: '',
        date: date
    })

    const [errorResponse, setErrorResponse] = useState({
        roomId: '',
        date: ''
    })
    useEffect(() => {
        if (date) {
            setCreatingData({...creatingData, date: date});
        }
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
            alert("Thêm thông tin ngày làm việc thành công")
            window.location.reload();
        }).catch(async (error) => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await handleCreateSchedule()
            }
            if(error.status === 400){
                setErrorResponse(error.response.data)

                if(error.response.data.message != null){
                    alert(error.response.data.message)
                }
            }
        })
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>
                <h1>Tạo thông tin lịch khám</h1>

                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td>{errorResponse.roomId}</td>
                    </tr>
                    <tr>
                        <td>Phòng khám:</td>
                        <td>
                            <select
                                onChange={(e) => setCreatingData({...creatingData, roomId: e.target.value})}>
                                <option value={""}>Chọn phòng làm việc</option>
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
                        <td>{errorResponse.date}</td>
                    </tr>
                    <tr>
                        <td>Ngày khám(Định dạng: tháng-ngày-năm):</td>
                        <td><input value={creatingData.date}
                                   onChange={(e) => setCreatingData({...creatingData, date: e.target.value})}
                                   type={"date"} min={formattedTomorrow}/></td>
                    </tr>
                    </tbody>
                </table>

                <button onClick={() => handleCreateSchedule()}>Lưu</button>
            </div>
        </div>
    );
}

const UpdateModal = ({isOpen, onClose, data, roomMap}) => {
    const [updatingData, setUpdatingData] = useState({...data});
    const [errorResponse, setErrorResponse] = useState({
        roomId: '',
        date: ''
    })
    useEffect(() => {
        if (data) {
            setUpdatingData({...data});
            setErrorResponse({roomId: '',
                date: ''})
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

        const token = await JwtService.getAccessToken()
        axios.put(WORKING_SCHEDULE.getUrlById(updatingData.id), JSON.stringify(updatingData), {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            countError = 0
            alert("Cập nhật lịch khám thành công")
            window.location.reload()
        }).catch(async (error) => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await handleUpdateSchedule()
            }
            if(error.status === 400){
                setErrorResponse(error.response.data)

                if(error.response.data.message != null){
                    alert(error.response.data.message)
                }
            }
        })
    }

    const handleDeleteSchedule = async (id) => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.delete(WORKING_SCHEDULE.getUrlById(id), {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            countError = 0
            alert("Xóa lịch làm việc thành công")
            window.location.reload()
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await handleUpdateSchedule()
            }
        })
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>
                <h1>Cập nhật thông tin lịch khám</h1>

                <table>{(() => {
                    const today = new Date()
                    const scheduleDate = new Date(updatingData.date)
                    if (today >= scheduleDate) {
                        return <tbody>
                                    <tr style={{display: "flex"}}>
                                        <td>Phòng khám:</td>
                                        <td>{roomMap.get(updatingData.roomId).name}</td>
                                    </tr>

                                    <tr style={{display: "flex"}}>
                                        <td>Ngày khám:</td>
                                        <td>{updatingData.date.split('-').reverse().join('-')}</td>
                                    </tr>
                                </tbody>
                    }
                    return <tbody>
                                <tr>
                                    <td></td>
                                    <td>{errorResponse.roomId}</td>
                                </tr>
                                <tr>
                                    <td>Phòng khám</td>
                                    <td>
                                        <select
                                            value={updatingData.roomId}
                                            onChange={(e) => setUpdatingData({
                                                ...updatingData,
                                                roomId: e.target.value
                                            })}>
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
                                    <td>{errorResponse.date}</td>
                                </tr>
                                <tr>
                                    <td>Ngày khám</td>
                                    <td>
                                        <input type={"date"} value={updatingData.date}
                                               onChange={(e) => setUpdatingData({
                                                   ...updatingData,
                                                   date: e.target.value
                                               })}
                                                min={formattedTomorrow}/>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <button onClick={() => handleUpdateSchedule()}>Lưu</button>
                                    </td>
                                    <td>
                                        <button onClick={() => handleDeleteSchedule(updatingData.id)}>Xóa lịch làm việc</button>
                                    </td>
                                </tr>
                    </tbody>
                })()}</table>
            </div>
        </div>
    );
};

const WorkingScheduleInMonth = () => {
    page = 'list'

    const [roomMap, setRoomMap] = useState(new Map())
    const [currentDate, setCurrentDate] = useState(new Date());

    const currentMonth = currentDate.getMonth(); // Lấy tháng hiện tại (0 - 11)
    const currentYear = currentDate.getFullYear();

    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    const firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();
    const days = Array.from({length: daysInMonth}, (_, index) => index + 1);

    //Click pre month
    const handlePreviousMonth = () => {
        setCurrentDate(new Date(currentYear, currentMonth - 1, 1));
    };

    //Click next month
    const handleNextMonth = () => {
        setCurrentDate(new Date(currentYear, currentMonth + 1, 1));
    };

    const calendarDays = [];
    let week = Array(firstDayOfMonth).fill(null); // Bắt đầu với các ô trống nếu tháng không bắt đầu từ Chủ Nhật

    //Get all room
    const getAllRooms = async () => {
        if (countError === 5)
            return
        const token = await JwtService.getAccessToken()
        axios.get(HOSPITAL_INFORMATION.EXAMINATION_ROOM.getUrl(), {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            countError = 0
            const map = new Map()
            response.data.forEach(item => map.set(item.id, item))
            setRoomMap(map)
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if (result) {
                countError++
                await getAllRooms()
            }
        })
    }


    //Get all Schedule
    const [schedules, setSchedules] = useState([])
    const getSchedules = async () => {
        if (countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(WORKING_SCHEDULE.getSchedulesInMonthOfEmployee(currentYear, currentMonth+1), {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            countError = 0
            setSchedules(response.data)
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await getSchedules()
            }
        })
    }

    useEffect(() => {
        const fetchData = async () => {
            await getSchedules()
            await getAllRooms()
        }
        fetchData()
    }, [page])

    days.forEach((day) => {
        week.push(day);
        if (week.length === 7) {
            calendarDays.push(week);
            week = [];
        }
    });

    // Thêm tuần cuối cùng nếu còn ngày
    if (week.length > 0) {
        calendarDays.push(week.concat(Array(7 - week.length).fill(null))); // Thêm các ô trống vào cuối tuần nếu chưa đủ 7 ngày
    }

    //Create Schedule
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

    //Update schedule
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
                <button onClick={handlePreviousMonth}>Tháng trước</button>
                <span>
          Tháng {currentMonth + 1} / {currentYear}
        </span>
                <button onClick={handleNextMonth}>Tháng sau</button>
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
                                    const dayTemp = currentYear + '-' + (currentMonth+1) + '-' + day
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
        axios.get(EMPLOYYEE.getUrl(), {
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
            <h2>Quản lý lịch làm việc</h2>
            <WorkingScheduleInMonth/>
        </div>
    );
}

function CreateWorkingSchedule() {
    page = 'create'
    const [roles, setRoles] = useState([])
    let countError = 0


    //Call api get all ROLES
    const getRoles = async () => {
        if (countError == 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(ROLE.getUrl(), {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        }).then(response => {
            countError = 0;
            setRoles(response.data);
        })
            .catch(async error => {
                console.error('API Error:', error);
                const result = await JwtService.checkTokenExpired(error)
                if(result){
                    await getRoles()
                }
                countError++
            });
    }

    useEffect(() => {
        getRoles()
    }, [page])

    const [selectedRoles, setSelectedRoles] = useState([]); // State để lưu danh sách role được chọn

    // Hàm để xử lý khi checkbox thay đổi
    const handleRoleChange = (role) => {
        if (selectedRoles.includes(role)) {
            setSelectedRoles(selectedRoles.filter(selectedRole => selectedRole !== role));
        } else {
            setSelectedRoles([...selectedRoles, role]);
        }
    };

    //Status bar
    const setStatus = useRef()

    const sendMessageToStatusBar = (message, index) => {
        console.log(message)
        if(setStatus.current){
            if(typeof message === "object"){
                setStatus.current.triggerChildFunction({...message,
                    index: index
                });
            }
            else if(typeof message === 'string'){
                setStatus.current.triggerChildFunction({...JSON.parse(message),
                    index: index
                });
            }
        }
    }

    useEffect(() => {
        const webSocket = new WebSocketService()
        const topics = [WEBSOCKET.topicCreateEmployee(JwtService.geUserFromToken()),
            WEBSOCKET.topicCreatedEmployee(JwtService.geUserFromToken())]
        webSocket.connectAndSubscribe(topics, (message, index) => {
            sendMessageToStatusBar (message, index)
        })
    }, [page]);

    //Send api create employee

    const [key, setKey] = useState(0)

    const createEmployee = async () => {
        //Refresh statusbar
        setKey(prevKey => prevKey + 1)

        if(countError == 5)
            return

        const employee = {
            introduce: description,
            numberPhone: phone,
            email: email,
            dateOfBirth: date,
            fullName: {
                firstName: firstName,
                middleName: middleName,
                lastName: lastName
            },
            permissions: selectedRoles.map(role => {
                return {
                    permission: role
                }
            })
        }
        console.log(employee)

        const token = await JwtService.getAccessToken()
        axios.post(AUTHENTICATION.createEmployee(), employee, {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        }).then(response => {
            setFirstNameError('')
            setMiddleNameError('')
            setLastNameError('')
            setPhoneError('')
            setDateError('')
            setEmailError('')
            setDescriptionError('')
            setPermisstionError('')
            countError = 0;
        })
            .catch(async error => {
                console.error('API Error:', error);
                const result = await JwtService.checkTokenExpired(error)
                if(result){
                    await createEmployee()
                }
                if(error.response.status === 400){
                    const data = error.response.data
                    console.log(data)
                    setFirstNameError(data['fullName.firstName'])
                    setMiddleNameError(data['fullName.middleName'])
                    setLastNameError(data['fullName.lastName'])
                    setPhoneError(data.numberPhone)
                    setDateError(data.dateOfBirth)
                    setEmailError(data.email)
                    setDescriptionError(data.introduce)
                    setPermisstionError(data.permissions)

                    if(data.message != null){
                        const errorMessage = {
                            message: data.message,
                            status: 'ERROR'
                        }
                        sendMessageToStatusBar(errorMessage, 0)
                    }
                }
                countError++
            });
    }

    const [firstName, setFirstName] = useState('')
    const [firstNameError, setFirstNameError] = useState('')

    const [middleName, setMiddleName] = useState('')
    const [middleNameError, setMiddleNameError] = useState('')

    const [lastName, setLastName] = useState('')
    const [lastNameError, setLastNameError] = useState('')

    const [phone, setPhone] = useState('')
    const [phoneError, setPhoneError] = useState('')

    const [email, setEmail] = useState('')
    const [emailError, setEmailError] = useState('')

    const [date, setDate] = useState('')
    const [dateError, setDateError] = useState('')

    const [description, setDescription] = useState('')
    const [descriptionError, setDescriptionError] = useState('')

    const [permissionsError, setPermisstionError] = useState('')

    return (
        <>
            <div>
                <Link to={'/admin/employee-management'}>Quản lý thông tin nhân viên</Link>
                <p> >> Thêm mới nhân viên</p>
            </div>

            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p>{firstNameError}</p></td>
                    </tr>
                    <tr>
                        <td><label>Tên</label></td>
                        <td><input value={firstName} onChange={e => setFirstName(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{lastNameError}</p></td>
                    </tr>
                    <tr>
                        <td><label>Họ</label></td>
                        <td><input value={lastName} onChange={e => setLastName(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{middleNameError}</p></td>
                    </tr>
                    <tr>
                        <td><label>Tên đệm</label></td>
                        <td><input value={middleName} onChange={e => setMiddleName(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{phoneError}</p></td>
                    </tr>
                    <tr>
                        <td><label>Số điện thoại</label></td>
                        <td><input value={phone} onChange={e => setPhone(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{emailError}</p></td>
                    </tr>
                    <tr>
                        <td><label>Email</label></td>
                        <td><input value={email} onChange={e => setEmail(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{dateError}</p></td>
                    </tr>
                    <tr>
                        <td>Ngày sinh</td>
                        <td><input type={"date"} value={date} onChange={e => setDate(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{descriptionError}</p></td>
                    </tr>
                    <tr>
                        <td>Giới thiệu</td>
                        <td><input value={description} onChange={e => setDescription(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{permissionsError}</p></td>
                    </tr>
                    <tr>
                        <td>Vai trò:</td>
                        {roles.map(role => (
                            <td key={role}>
                                <label>
                                    <input
                                        type="checkbox"
                                        value={role}
                                        checked={selectedRoles.includes(role)}
                                        onChange={() => handleRoleChange(role)}
                                    />
                                    {role}
                                </label>
                            </td>
                        ))}
                    </tr>

                    <tr>
                        <td>
                            <button onClick={createEmployee}>Lưu</button>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <div>
                    <h1>Quá trình</h1>
                    <StatusBar numberOfCircles={2} labels={['Tạo tài khoản','Tạo thông tin nhân viên']}
                               callBack={setStatus}
                               key={key}
                    />
                </div>
            </div>
        </>
    )
}

export {WorkingScheduleManagement, CreateWorkingSchedule}