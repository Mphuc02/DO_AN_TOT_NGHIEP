import {Link, useNavigate} from "react-router-dom";
import styles from "../../layouts/body/style.module.css";
import iconUpdate from "../../imgs/update.png";
import {useEffect, useState} from "react";
import axios from "axios";
import {MEDICINE} from "../../ApiConstant";
import {JwtService} from "../../service/JwtService";
import RoutesConstant from "../../RoutesConstant";

let countError = 0
let page = ''
const OriginManagement = () => {
    page = 'list'
    const navigate = useNavigate()

    const [origins, setOrigins] = useState([])
    const getOrigins = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(MEDICINE.Origin.getUrl(), {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            console.log(response)
            countError = 0
            setOrigins(response.data)
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                await getOrigins()
                countError++
            }
        })
    }

    useEffect(() => {
        getOrigins()
    }, [page]);

    return (
        <>
            <h2 className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin thông tin Nguồn gốc thuôc</h2>
            <Link to={'create'}
                  className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">Thêm thông tin Nguồn gốc</Link>
            <table
                className="table-auto border-collapse border border-gray-300 w-full text-left mt-10"
                border={1}>
                <thead
                    className="bg-gray-200">
                <tr>
                    <th className="border border-gray-300 px-4 py-2">Id</th>
                    <th className="border border-gray-300 px-4 py-2">Tên</th>
                    <th className="border border-gray-300 px-4 py-2">Mô tả</th>
                </tr>
                </thead>
                <tbody>
                {origins.map((origin, index) => (
                    <tr
                        className="hover:cursor-pointer hover:bg-gray-100 transition duration-200"
                        onClick={() => navigate(RoutesConstant.ADMIN.UPDATE_ORIGIN_MEDICINE(origin.id))}
                        key={index}>
                        <td className="border border-gray-300 px-4 py-2 text-center">{origin.id}</td>
                        <td className="border border-gray-300 px-4 py-2 text-center">{origin.name}</td>
                        <td className="border border-gray-300 px-4 py-2 text-center">{origin.description}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    )
}

const CreateOrigin = () => {
    page = 'create'

    const [originError, setOriginError] = useState({})
    const [origin, setOrigin] = useState({
        name: '',
        description: ''
    })

    const createOrigin = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.post(MEDICINE.Origin.getUrl(), JSON.stringify(origin), {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            countError = 0
            console.log(response)
            alert('Thêm mới thông tin thành công')
            setOrigin({name: '', description: ''})
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                await createOrigin()
                countError++
            }
            else if(error.status === 400){
                setOriginError(error.response.data)
            }
        })
    }

    const handleCreateOrigin = () => {
        console.log(origin)
        createOrigin()
    }

    return (
        <>
            <div>
                <Link
                    className="text-xl font-bold text-green-600 mb-4"
                    to={RoutesConstant.ADMIN.MEDICINE_MANAGEMENT_ORIGIN}>Quản lý thông tin Nguồn gốc thuốc</Link>
                <span className="text-xl font-bold text-green-600 mb-4"> ⟶ Thêm mới thông tin Nguồn gốc thuốc</span>
            </div>
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{originError.name}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Tên</td>
                        <td><input
                            className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                            value={origin.name} onChange={(e) => {
                            setOrigin({...origin, name: e.target.value})
                        }}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{originError.description}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Mô tả</td>
                        <td><input
                            className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                            value={origin.description}
                                   onChange={e => setOrigin({...origin, description: e.target.value})}/></td>
                    </tr>
                    </tbody>
                </table>

                <button
                    className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                    onClick={() => handleCreateOrigin()}>Lưu</button>
            </div>
        </>
    )
}

export {OriginManagement, CreateOrigin}