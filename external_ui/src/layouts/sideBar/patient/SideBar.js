import styles from '../SideBar.module.css'
import {Link} from 'react-router-dom';
import RoutesConstant from "../../../RoutesConstant";

function SideBar(){

    return(
        <ul className={styles.SideBar}>
            <li><Link to={RoutesConstant.PATIENT.DIAGNOSTICS}>Chuẩn đoán bệnh</Link></li>
            <li><Link to={"/patient/appointments-management"}>Quản lý lịch hẹn</Link></li>
            <li><Link to={"/patient/examination-histories"}>Lịch sử khám bệnh</Link></li>
            <li><Link to={RoutesConstant.PATIENT.CHAT}>Tin nhắn</Link> </li>
        </ul>
    )
}

export default SideBar