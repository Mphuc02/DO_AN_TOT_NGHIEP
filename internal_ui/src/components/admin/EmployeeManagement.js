import {useState, useEffect, useRef } from "react";
import {AUTHENTICATION, EMPLOYYEE, ROLE, WEBSOCKET} from "../../Constant";
import axios from "axios";
import {JwtService} from "../../service/JwtService";
import {Link} from "react-router-dom";
import WebSocketService from "../../service/WebSocketService";
import StatusBar from "../common/StatusBar";
import styles from '../../layouts/body/style.module.css'
import update from '../../imgs/update.png'

let page
function EmployeeManagement(){
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
            <h2>Quản lý thông tin nhân viên</h2>
            <Link to={'create'}>Thêm mới nhân viên</Link>
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ và tên</th>
                        <th>Giới thiệu</th>
                        <th>Ngày sinh(yyyy-MM-dd)</th>
                        <th>Vai trò</th>
                        <th>Chỉnh sửa</th>
                    </tr>
                </thead>
                <tbody>
                    {data.map((user, index) => (
                        <tr key={index}>
                            <td>{user.id}</td>
                            <td>{user.fullName.firstName + " " + user.fullName.middleName + " " + user.fullName.lastName}</td>
                            <td>{user.introduce}</td>
                            <td>{user.date}</td>
                            <td>{user.permissions.reduce((total, cur) => {
                                    return total  + ROLE.getRole(cur) + ", "
                                }, '')}</td>
                            <td>
                                <Link className={styles.updateLink} to={'update/' + user.id}>
                                    <img className={styles.update_icon} src={update}/>
                                </Link>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

function CreateEmployee() {
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

export {EmployeeManagement, CreateEmployee}