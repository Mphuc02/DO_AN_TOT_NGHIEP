import Header from "../../header/doctor/Header";
import SideBar from "../../sideBar/doctor/SideBar";
import styles from '../style.module.css'
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import RoutesConstant from "../../../RoutesConstant";

function AdminLayout({children}){
    const [doctor, setDoctor] = useState(null)
    const navigate = useNavigate()

    useEffect(() => {
        const doctor = JSON.parse(localStorage.getItem('doctor'))

        if(!doctor || !doctor.roles.includes('DOCTOR')){
            localStorage.removeItem('access-token')
            localStorage.removeItem('refresh-token')
            localStorage.removeItem('doctor')

            navigate(RoutesConstant.DOCTOR.LOGIN)
        }else {
            setDoctor(doctor)
        }
    }, []);

    return(
        <>
            <nav className="fixed top-0 z-50 w-full bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700">
                <Header doctor={doctor} />
            </nav>
            <aside
                id="logo-sidebar"
                className="fixed top-0 left-0 z-40 w-64 h-screen pt-20 transition-transform -translate-x-full bg-white border-r border-gray-200 sm:translate-x-0 dark:bg-gray-800 dark:border-gray-700"
                aria-label="Sidebar">
                <div className="h-full px-3 pb-4 overflow-y-auto bg-white dark:bg-gray-800">
                    <SideBar />
                </div>
            </aside>
            <div className="sm:ml-64">
                <div className="border-2 border-gray-200 border-dashed rounded-lg dark:border-gray-700 mt-14">
                    {children}
                </div>
            </div>
        </>
    )
}

export default AdminLayout