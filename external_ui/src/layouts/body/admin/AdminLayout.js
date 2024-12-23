import Header from "../../header/admin/Header";
import SideBar from "../../sideBar/admin/SideBar";
import styles from '../style.module.css'
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import RoutesConstant from "../../../RoutesConstant";

function AdminLayout({children}){
    const [admin, setAdmin] = useState(null)
    const navigate = useNavigate()



    useEffect(() => {
        const admin = JSON.parse(localStorage.getItem('admin'))
        if(!admin || !admin.roles.includes('ADMIN')){
            localStorage.removeItem('access-token')
            localStorage.removeItem('refresh-token')
            localStorage.removeItem('admin')

            navigate(RoutesConstant.ADMIN.LOGIN)
        }else{
            setAdmin(admin)
        }
    }, []);

    return(
        <div className={styles.AllScreen}>
            <div className={styles.Header}>
                <Header admin={admin}/>
            </div>

            <div className={styles.SideBar}>
                <SideBar/>
            </div>

            <div className={styles.Children}>
                {children}
            </div>
        </div>

    )
}

export default AdminLayout