import {Link, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {JwtService} from "../../service/JwtService";
import axios from "axios";
import {HOSPITAL_INFORMATION} from "../../ApiConstant";
import routesConstant from "../../RoutesConstant";
import RoutesConstant from "../../RoutesConstant";

let page = ''
let countError = 0
const DiseaseManagement = () => {
    page = 'list'
    const navigate = useNavigate()
    const [rooms, setRooms] = useState([])

    const getRooms = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(HOSPITAL_INFORMATION.DISEASES.getUrl(), {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            console.log(response)
            setRooms(response.data)
            countError = 0
        }).catch(async (error) => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            console.log(result)
            if(result){
                countError++
                await getRooms()
            }
        })
    }

    useEffect(() => {
        getRooms()
    }, [page]);

    return (
        <>
            <h2
                className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin thông tin bệnh</h2>
            <Link
                className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
                to={'create'}>Thêm thông tin bệnh</Link>
            <table border={1}
                   className="table-auto border-collapse border border-gray-300 w-full text-left mt-10">
                <thead className="bg-gray-200">
                <tr>
                    <th className="border border-gray-300 px-4 py-2">Id</th>
                    <th className="border border-gray-300 px-4 py-2">Tên</th>
                </tr>
                </thead>
                <tbody>
                {rooms.map((room, index) => (
                    <tr
                        className="hover:cursor-pointer hover:bg-gray-100 transition duration-200"
                        key={index}>
                        <td className="border border-gray-300 px-4 py-2 text-center">{room.id}</td>
                        <td className="border border-gray-300 px-4 py-2 text-center">{room.name}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    )
}


const CreateDisease = () => {
    page = 'create'

    const [roomError, setRoomError] = useState({})
    const [room, setRoom] = useState({
        name: ''
    })

    const handleCreateRoom = async () => {
        if(countError === 5)
            return
        console.log(room)
        const token = await JwtService.getAccessToken()
        axios.post(HOSPITAL_INFORMATION.DISEASES.getUrl(), JSON.stringify(room), {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            console.log(response)
            alert('Thêm thông tin thành công')
            window.location.reload()
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError = 0
                handleCreateRoom()
            }
            if(error.status === 400){
                setRoomError(error.response.data)
            }
        })
    }

    return (
        <>
            <div>
                <Link to={RoutesConstant.ADMIN.DISEASE_MANAGEMENT}
                      className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin bệnh</Link>
                <span
                    className="text-xl font-bold text-green-600 mb-4"> ⟶ Thêm mới thông tin bệnh</span>
            </div>
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{roomError.name}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Tên</td>
                        <td>
                            <input
                                className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                value={room.name} onChange={(e) => {
                                setRoom({...room, name: e.target.value})
                            }}/>
                        </td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Mô tả:</td>
                        <td>
                            <input
                                className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                value={room.description} onChange={(e) => {
                                setRoom({...room, description: e.target.value})
                            }}/>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <button
                    className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                    onClick={() => handleCreateRoom()}>Lưu
                </button>
            </div>
        </>
    )
}

export {DiseaseManagement, CreateDisease}