import {useState, useEffect, useRef } from "react";
import {AUTHENTICATION, EMPLOYYEE, ROLE, WEBSOCKET} from "../../ApiConstant";
import {JwtService} from "../../service/JwtService";
import {Link, useNavigate} from "react-router-dom";
import WebSocketService from "../../service/WebSocketService";
import StatusBar from "../common/StatusBar";
import styles from '../../layouts/body/style.module.css'
import update from '../../imgs/update.png'
import {SendApiService} from "../../service/SendApiService";
import RoutesConstant from "../../RoutesConstant";

let page
function EmployeeManagement(){
    page = 'list'
    const navigate = useNavigate()
    const [data, setData] = useState([]);

    const getEmployees = async () => {
        await SendApiService.getRequest(EMPLOYYEE.getUrl(''),  {}, (response) => {
            setData(response.data)
        }, (error) => {

        })
    }

    useEffect(() => {
        getEmployees()
    }, []);

    return (
        <div>
            <h2 className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin nhân viên</h2>
            <Link to={'create'}
                  className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">Thêm mới nhân viên</Link>
            <table border="1"
                   className="table-auto border-collapse border border-gray-300 w-full text-left mt-10">
                <thead
                    className="bg-gray-200">
                    <tr>
                        <th className="border border-gray-300 px-4 py-2">ID</th>
                        <th className="border border-gray-300 px-4 py-2">Họ và tên</th>
                        <th className="border border-gray-300 px-4 py-2">Giới thiệu</th>
                        <th className="border border-gray-300 px-4 py-2">Ngày sinh(yyyy-MM-dd)</th>
                        <th className="border border-gray-300 px-4 py-2">Vai trò</th>
                    </tr>
                </thead>
                <tbody>
                    {data.map((user, index) => (
                        <tr
                            className="hover:cursor-pointer hover:bg-gray-100 transition duration-200"
                            onClick={() => navigate(RoutesConstant.ADMIN.UPDATE_EMPLOYEE(user.id))}
                            key={index}>
                            <td className="border border-gray-300 px-4 py-2 text-center">{user.id}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{user.fullName.firstName + " " + user.fullName.middleName + " " + user.fullName.lastName}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{user.introduce}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{user.date}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{user.roles.reduce((total, cur) => {
                                    return total + ROLE.getRole(cur) + ", "
                                }, '')}
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
        await SendApiService.getRequest(ROLE.getUrl(), {}, (response) => {
            setRoles(response.data);
        }, (error) => {

        })
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
                    role: role
                }
            })
        }
        console.log(employee)

        await SendApiService.postRequest(AUTHENTICATION.createEmployee(), employee, {}, (response) => {
            setFirstNameError('')
            setMiddleNameError('')
            setLastNameError('')
            setPhoneError('')
            setDateError('')
            setEmailError('')
            setDescriptionError('')
            setRoleError('')
        }, (error) => {
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
                setRoleError(data.role)

                if(data.message != null){
                    const errorMessage = {
                        message: data.message,
                        status: 'ERROR'
                    }
                    sendMessageToStatusBar(errorMessage, 0)
                }
            }
        })
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

    const [roleError, setRoleError] = useState('')

    return (
        <>
            <div>
                <Link to={'/admin/employee-management'}
                      className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin nhân viên</Link>
                <span className="text-xl font-bold text-green-600 mb-4"> ⟶ Thêm mới nhân viên</span>
            </div>

            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{lastNameError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4"><label>Họ</label></td>
                        <td className="pl-1">
                            <input
                                className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                                    value={firstName} onChange={e => setFirstName(e.target.value)}/>
                        </td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{middleNameError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4"><label>Tên đệm</label>
                        </td>
                        <td><input value={middleName}
                                   className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                   onChange={e => setMiddleName(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{firstNameError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4"><label>Tên</label></td>
                        <td><input value={lastName}
                                   className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                   onChange={e => setLastName(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{phoneError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4"><label>Số điện
                            thoại</label></td>
                        <td><input value={phone}
                                   className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                   onChange={e => setPhone(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{emailError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4"><label>Email</label>
                        </td>
                        <td><input value={email}
                                   className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                   onChange={e => setEmail(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{dateError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Ngày sinh</td>
                        <td><input type={"date"}
                                   className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                   value={date} onChange={e => setDate(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{descriptionError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Giới thiệu</td>
                        <td><input value={description}
                                   className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                   onChange={e => setDescription(e.target.value)}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{roleError}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Vai trò:</td>
                        {roles.map(role => (
                            <td key={role}>
                                <label>
                                    <input
                                        type="checkbox"
                                        value={role}
                                        checked={selectedRoles.includes(role)}
                                        onChange={() => handleRoleChange(role)}
                                    />
                                    <span className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">{role}</span>
                                </label>
                            </td>
                        ))}
                    </tr>

                    <tr>
                        <td>
                            <button
                                className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                                onClick={createEmployee}>Lưu</button>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <div>
                    <h1 className="pl-2 pr-1 font-medium text-gray-700 text-left mb-4">Quá trình thực hiện</h1>
                    <StatusBar numberOfCircles={2} labels={['Tạo tài khoản', 'Tạo thông tin nhân viên']}
                               callBack={setStatus}
                               key={key}
                    />
                </div>
            </div>
        </>
    )
}

export {EmployeeManagement, CreateEmployee}