import {Link, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {MEDICINE} from "../../ApiConstant";
import {JwtService} from "../../service/JwtService";
import iconUpdate from '../../imgs/update.png'
import styles from '../../layouts/body/style.module.css'
import RoutesConstant from "../../RoutesConstant";

let countError = 0;
let page
const MedicineManagement = () => {
    page = 'list'
    const [medicines, setMedicines] = useState([])
    const navigate = useNavigate()

    const getMedicines = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(MEDICINE.Medicine.getUrl(), {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        }).then(response => {
            console.log(response)
            setMedicines(response.data)
        }).catch(async (error) => {
            console.error('API Error:', error);
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                await getMedicines()
            }
        })
    }

    useEffect(() => {
        getMedicines()
    }, [page]);


    return (
        <>
            <h2
                className="text-xl font-bold text-green-600 mb-4" >Quản lý thông tin thông tin thuốc</h2>
            <Link to={'create'}
                  className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">Thêm thông tin thuốc</Link>
            <table border={1}
                   className="table-auto border-collapse border border-gray-300 w-full text-left mt-10">
                <thead
                    className="bg-gray-200">
                    <tr>
                        <th className="border border-gray-300 px-4 py-2">Id</th>
                        <th className="border border-gray-300 px-4 py-2">Tên</th>
                        <th className="border border-gray-300 px-4 py-2">Giá bán hiện tại</th>
                        <th className="border border-gray-300 px-4 py-2">Số lượng còn trong kho</th>
                        <th className="border border-gray-300 px-4 py-2">Nguồn gốc</th>
                    </tr>
                </thead>
                <tbody>
                    {medicines.map((medicine, index) => (
                        <tr key={index}
                            className="hover:cursor-pointer hover:bg-gray-100 transition duration-200"
                            onClick={() => navigate(RoutesConstant.ADMIN.UPDATE_MEDICINE(medicine.id))}>
                            <td className="border border-gray-300 px-4 py-2 text-center">{medicine.id}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{medicine.name}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{medicine.price}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{medicine.quantity}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{medicine.origin.name}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </>
    )
}

const CreateMedicine = () => {
    page = 'create'

    const [origins, setOrigins] = useState([])
    const [medicine, setMedicice] = useState({
        name: "",
        description: "",
        originId: "",
        price: ""
    })

    const [medicineError, setMedicineError] = useState({
        name: "",
        description: "",
        originId: "",
        price: ""
    })

    const createMedicine = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.post(MEDICINE.Medicine.getUrl(), JSON.stringify(medicine), {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            console.log(response)
            countError = 0
            alert("Thêm thông tin thuốc thành công")
        }).catch(async error => {
            console.error('API Error:', error);
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                await createMedicine()
                countError++
            }
            else if(error.status === 400){
                if(error.response != null){
                    setMedicineError(error.response.data)
                }
            }
        })
    }

    const getOrigins = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(MEDICINE.Origin.getUrl(), {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        }).then(response => {
            countError = 0
            console.log(response)
            setOrigins(response.data)
        }).catch(async error => {
            console.error('API Error:', error);
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                await createMedicine()
                countError++
            }
        })
    }

    useEffect(() => {
        getOrigins()
    }, [page])

    const handleCreateMedicine = () => {
        console.log(medicine)
        createMedicine()
    }

    return(
        <>
            <div>
                <Link to={'/admin/medicine-management'}
                      className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin thuốc</Link>
                <span className="text-xl font-bold text-green-600 mb-4"> ⟶ Thêm mới thông tin thuốc</span>
            </div>
            <div>
                <table>
                    <tbody>
                        <tr>
                            <td></td>
                            <td><p className="text-red-500 text-sm font-medium">{medicineError.name}</p></td>
                        </tr>
                        <tr>
                            <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Tên</td>
                            <td><input
                                className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                value={medicine.name} onChange={(e) => {
                                setMedicice({...medicine, name: e.target.value})
                            }}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td><p className="text-red-500 text-sm font-medium">{medicineError.price}</p></td>
                        </tr>
                        <tr>
                            <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Giá bán</td>
                            <td><input type={"number"} value={medicine.price}
                                       className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                onChange={(e) => {
                                    setMedicice({...medicine, price: e.target.value})
                                }}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td><p className="text-red-500 text-sm font-medium">{medicineError.description}</p></td>
                        </tr>
                        <tr>
                            <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Mô tả</td>
                            <td><input value={medicine.description}
                                       className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                onChange={e => setMedicice({...medicine, description: e.target.value})}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td><p className="text-red-500 text-sm font-medium">{medicineError.originId}</p></td>
                        </tr>
                        <tr>
                            <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Nguồn gốc:</td>
                            {origins.map((origin, index) => (
                                <td key={index}>
                                    <label>
                                        <input type={"radio"}
                                               name="originGroup"
                                               value={origin.id}
                                               onChange={e => setMedicice({...medicine, originId: e.target.value})}
                                        />
                                        {origin.name}
                                    </label>
                                </td>
                            ))}
                        </tr>
                    </tbody>
                </table>

                <button
                    className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                    onClick={() => handleCreateMedicine()}>Lưu</button>
            </div>
        </>
    )
}

const UpdateMedicine = () => {
    page = 'update'
    const medicineId = window.location.pathname.split('/').pop()
    console.log(medicineId)
    countError = 0
    const [origins, setOrigins] = useState([])
    const [medicine, setMedicine] = useState({
        name: "",
        description: "",
        price: "",
        originId: ""
    })
    const [medicineError, setMedicineError] = useState({
        name: "",
        description: "",
        price: "",
        originId: ""
    })

    const getAllOrigins = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        await axios.get(MEDICINE.Origin.getUrl(), {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            countError = 0
            console.log(response)
            setOrigins(response.data)
        }).catch(async error => {
            console.error('API Error:', error);
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await getAllOrigins()
            }
        })
    }

    const getMedicine = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(MEDICINE.Medicine.getById(medicineId), {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            countError = 0
            console.log(response)
            setMedicine({...response.data, originId: response.data.origin.id})
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                await getMedicine()
                countError++
            }
        })
    }

    useEffect(() => {
        getAllOrigins()
        getMedicine()
    }, [page]);

    const handleUpdateRequest = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.put(MEDICINE.Medicine.getById(medicineId), JSON.stringify(medicine), {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    console.log(response)
                    alert("Cập nhật thông tin thành công")
                    setMedicine({...response.data, originId: response.data.origin.id})
                }).catch(async error => {
                    console.log(error)
                    const result = await JwtService.checkTokenExpired(error)
                    if(result){
                        await handleUpdateRequest()
                        countError++
                    }else if(error.status === 400){
                        if(error.response.data != null){
                            setMedicineError(error.response.data)
                        }
                    }
                })
    }

    return (
        <>
            <div>
                <Link to={'/admin/medicine-management'}
                      className="text-xl font-bold text-green-600 mb-4">Quản lý thông tin thuốc</Link>
                <span className="text-xl font-bold text-green-600 mb-4"> ⟶ Cập nhật thông tin thuốc</span>
            </div>
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{medicineError.name}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Tên</td>
                        <td><input
                            className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                            value={medicine.name} onChange={(e) => {
                            setMedicine({...medicine, name: e.target.value})
                        }}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{medicineError.price}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Giá bán</td>
                        <td><input
                            className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                            type={"number"} value={medicine.price}
                                   onChange={(e) => {
                                       setMedicine({...medicine, price: e.target.value})
                                   }}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{medicineError.description}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Mô tả</td>
                        <td><input
                            className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                            value={medicine.description}
                                   onChange={e => setMedicine({...medicine, description: e.target.value})}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p className="text-red-500 text-sm font-medium">{medicineError.originId}</p></td>
                    </tr>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Nguồn gốc</td>
                        {origins.map((origin, index) => (
                            <td key={index}>
                                <label>
                                    <input type={"radio"}
                                           name="originGroup"
                                           checked={origin.id === medicine.originId}
                                           value={origin.id}
                                           onChange={e => setMedicine({...medicine, originId: e.target.value})}
                                    />
                                    {origin.name}
                                </label>
                            </td>
                        ))}
                    </tr>
                    </tbody>
                </table>

                <button
                    className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                    onClick={() => handleUpdateRequest()}>Lưu</button>
            </div>
        </>
    )
}

export {MedicineManagement, CreateMedicine, UpdateMedicine}