import {Link, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {JwtService} from "../../service/JwtService";
import axios from "axios";
import {HOSPITAL_INFORMATION, PaymentApi} from "../../ApiConstant";
import routesConstant from "../../RoutesConstant";
import RoutesConstant from "../../RoutesConstant";
import {FormatDate} from "../../service/TimeService";
import {SendApiService} from "../../service/SendApiService";

let page = ''
let countError = 0
const ExaminationCostManagement = () => {
    page = 'list'
    const navigate = useNavigate()
    const [rooms, setRooms] = useState([])

    const getRooms = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(PaymentApi.EXAMINATION_COST_URL(), {
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
                className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin giá khám bệnh</h2>
            <Link
                className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
                to={'create'}>Thêm thông tin giá khám bệnh</Link>
            <table border={1}
                   className="table-auto border-collapse border border-gray-300 w-full text-left mt-10">
                <thead className="bg-gray-200">
                <tr>
                    <th className="border border-gray-300 px-4 py-2">Id</th>
                    <th className="border border-gray-300 px-4 py-2">Gía</th>
                    <td className="border border-gray-300 px-4 py-2">Ngày áp dụng</td>
                </tr>
                </thead>
                <tbody>
                {rooms.map((room, index) => (
                    <tr
                        className="hover:cursor-pointer hover:bg-gray-100 transition duration-200"
                        key={index}>
                        <td className="border border-gray-300 px-4 py-2 text-center">{room.id}</td>
                        <td className="border border-gray-300 px-4 py-2 text-center">{room.cost}</td>
                        <td className="border border-gray-300 px-4 py-2 text-center">{FormatDate(room.appliedAt)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    )
}


const CreateCost = () => {
    page = 'create'

    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const formattedTomorrow = tomorrow.toISOString().split('T')[0];

    const [roomError, setRoomError] = useState({})
    const [room, setRoom] = useState({
        name: ''
    })

    const handleCreateRoom = async () => {
        SendApiService.postRequest(PaymentApi.EXAMINATION_COST_URL(), room, {}, response => {
            alert('Thêm mới giá bán thành công')
            window.location.reload()
        }, error => {
            if(error.status === 400){
                if(error.response.data){
                    // const message = JSON.parse(error.response.data)
                    if(error.response.data.message){
                        alert('Lỗi: ' + error.response.data.message)
                    }
                }
            }
        })
    }

    return (
        <>
            <div>
                <Link to={RoutesConstant.ADMIN.EXAMINATION_COST_MANAGEMENT}
                      className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin giá khám bệnh</Link>
                <span
                    className="text-xl font-bold text-green-600 mb-4"> ⟶ Thêm mới thông tin giá khám bệnh</span>
            </div>
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{roomError.cost}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Gía khám</td>
                        <td>
                            <input
                                className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                value={room.name} onChange={(e) => {
                                setRoom({...room, cost: e.target.value})
                            }}/>
                        </td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{roomError.appliedAt}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Ngày áp dụng:</td>
                        <td>
                            <input
                                className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                type={"date"}
                                min={formattedTomorrow}
                                value={room.appliedAt} onChange={(e) => {
                                setRoom({...room, appliedAt: e.target.value})
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

export {ExaminationCostManagement, CreateCost}