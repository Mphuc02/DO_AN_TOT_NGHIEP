import {Link} from "react-router-dom";
import styles from "../../layouts/body/style.module.css";
import iconUpdate from "../../imgs/update.png";
import {useEffect, useState} from "react";
import {HOSPITAL_INFORMATION} from '../../ApiConstant'
import axios from "axios";
import {JwtService} from "../../service/JwtService";

let page = ''
let countError = 0
const ExaminationRoomManagement = () => {
    page = 'list'

    const [rooms, setRooms] = useState([])

    const getRooms = async () => {
        if(countError === 5)
            return

        const token = await JwtService.getAccessToken()
        axios.get(HOSPITAL_INFORMATION.EXAMINATION_ROOM.getUrl(), {
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
            <h2>Quản lý thông tin thông tin phòng khám bệnh</h2>
            <Link to={'create'}>Thêm thông tin phòng khám</Link>
            <table border={1}>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Tên</th>
                    <th>Chỉnh sửa</th>
                </tr>
                </thead>
                <tbody>
                {rooms.map((room, index) => (
                    <tr key={index}>
                        <td>{room.id}</td>
                        <td>{room.name}</td>
                        <td><Link className={styles.updateLink} to={'update/' + room.id}><img className={styles.update_icon} src={iconUpdate}/></Link></td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
        )
}

const CreateExaminationRoom = () => {
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
        axios.post(HOSPITAL_INFORMATION.EXAMINATION_ROOM.getUrl(), JSON.stringify(room), {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            console.log(response)
            alert('Thêm thông tin thành công')
            setRoom({name: ''})
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
                <Link to={'/admin/medicine-management'}>Quản lý thông tin phòng khám bệnh</Link>
                <p> >> Thêm mới thông tin phòng khám</p>
            </div>
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td></td>
                        <td><p>{roomError.name}</p></td>
                    </tr>
                    <tr>
                        <td>Tên</td>
                        <td><input value={room.name} onChange={(e) => {
                            setRoom({...room, name: e.target.value})
                        }}/></td>
                    </tr>
                    </tbody>
                </table>

                <button onClick={() => handleCreateRoom()}>Lưu</button>
            </div>
        </>
    )
}

export {ExaminationRoomManagement, CreateExaminationRoom}