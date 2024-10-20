import {Link} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {MEDICINE} from "../../Constant";
import {JwtService} from "../../service/JwtService";
import iconUpdate from '../../imgs/update.png'
import styles from '../../layouts/body/style.module.css'

let countError = 0;
let page
const MedicineManagement = () => {
    page = 'list'
    const [medicines, setMedicines] = useState([])

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
            <h2>Quản lý thông tin thông tin thuốc</h2>
            <Link to={'create'}>Thêm thông tin thuốc</Link>
            <table border={1}>
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Tên</th>
                        <th>Gía bán hiện tại</th>
                        <th>Số lượng còn trong kho</th>
                        <th>Nguồn gốc</th>
                        <th>Chỉnh sửa</th>
                    </tr>
                </thead>
                <tbody>
                    {medicines.map((medicine, index) => (
                        <tr key={index}>
                            <td>{medicine.id}</td>
                            <td>{medicine.name}</td>
                            <td>{medicine.price}</td>
                            <td>{medicine.quantity}</td>
                            <td>{medicine.origin.name}</td>
                            <td><Link className={styles.updateLink} to={'update/' + medicine.id}><img className={styles.update_icon} src={iconUpdate}/></Link></td>
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
                <Link to={'/admin/medicine-management'}>Quản lý thông tin thuốc</Link>
                <p> >> Thêm mới thông tin thuốc</p>
            </div>
            <div>
                <table>
                    <tbody>
                        <tr>
                            <td></td>
                            <td><p>{medicineError.name}</p></td>
                        </tr>
                        <tr>
                            <td>Tên</td>
                            <td><input value={medicine.name} onChange={(e) => {
                                setMedicice({...medicine, name: e.target.value})
                            }}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td><p>{medicineError.price}</p></td>
                        </tr>
                        <tr>
                            <td>Gía bán</td>
                            <td><input type={"number"} value={medicine.price}
                                onChange={(e) => {
                                    setMedicice({...medicine, price: e.target.value})
                                }}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td><p>{medicineError.description}</p></td>
                        </tr>
                        <tr>
                            <td>Mô tả</td>
                            <td><input value={medicine.description}
                                onChange={e => setMedicice({...medicine, description: e.target.value})}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td><p>{medicineError.originId}</p></td>
                        </tr>
                        <tr>
                            <td>Nguồn gốc</td>
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

                <button onClick={() => handleCreateMedicine()}>Lưu</button>
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
                <Link to={'/admin/medicine-management'}>Quản lý thông tin thuốc</Link>
                <p> >> Cập nhật thông tin thuốc</p>
            </div>
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p>{medicineError.name}</p></td>
                    </tr>
                    <tr>
                        <td>Tên</td>
                        <td><input value={medicine.name} onChange={(e) => {
                            setMedicine({...medicine, name: e.target.value})
                        }}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{medicineError.price}</p></td>
                    </tr>
                    <tr>
                        <td>Gía bán</td>
                        <td><input type={"number"} value={medicine.price}
                                   onChange={(e) => {
                                       setMedicine({...medicine, price: e.target.value})
                                   }}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{medicineError.description}</p></td>
                    </tr>
                    <tr>
                        <td>Mô tả</td>
                        <td><input value={medicine.description}
                                   onChange={e => setMedicine({...medicine, description: e.target.value})}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{medicineError.originId}</p></td>
                    </tr>
                    <tr>
                        <td>Nguồn gốc</td>
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

                <button onClick={() => handleUpdateRequest()}>Lưu</button>
            </div>
        </>
    )
}

export {MedicineManagement, CreateMedicine, UpdateMedicine}