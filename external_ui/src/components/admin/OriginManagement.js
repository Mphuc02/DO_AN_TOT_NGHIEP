import {Link} from "react-router-dom";
import styles from "../../layouts/body/style.module.css";
import iconUpdate from "../../imgs/update.png";
import {useEffect, useState} from "react";
import axios from "axios";
import {MEDICINE} from "../../ApiConstant";
import {JwtService} from "../../service/JwtService";

let countError = 0
let page = ''
const OriginManagement = () => {
    page = 'list'

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
            <h2>Quản lý thông tin thông tin Nguồn gốc thuôc</h2>
            <Link to={'create'}>Thêm thông tin Nguồn gốc</Link>
            <table border={1}>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Tên</th>
                    <th>Mô tả</th>
                    <th>Chỉnh sửa</th>
                </tr>
                </thead>
                <tbody>
                {origins.map((origin, index) => (
                    <tr key={index}>
                        <td>{origin.id}</td>
                        <td>{origin.name}</td>
                        <td>{origin.description}</td>
                        <td><Link className={styles.updateLink} to={'update/' + origin.id}><img className={styles.update_icon} src={iconUpdate}/></Link></td>
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
                <Link to={'/admin/medicine-management'}>Quản lý thông tin Nguồn gốc thuốc</Link>
                <p> >> Thêm mới thông tin Nguồn gốc thuốc</p>
            </div>
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p>{originError.name}</p></td>
                    </tr>
                    <tr>
                        <td>Tên</td>
                        <td><input value={origin.name} onChange={(e) => {
                            setOrigin({...origin, name: e.target.value})
                        }}/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><p>{originError.description}</p></td>
                    </tr>
                    <tr>
                        <td>Mô tả</td>
                        <td><input value={origin.description}
                                   onChange={e => setOrigin({...origin, description: e.target.value})}/></td>
                    </tr>
                    </tbody>
                </table>

                <button onClick={() => handleCreateOrigin()}>Lưu</button>
            </div>
        </>
    )
}

export {OriginManagement, CreateOrigin}