import SideBar from '../../sideBar/patient/SideBar'
import Header from "../../header/patient/Header";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import RoutesConstant from "../../../RoutesConstant";

function PatientLayout({children}){
    const [patient, setPatient] = useState(null)
    const navigate = useNavigate()

    useEffect(() => {
        const json = localStorage.getItem('patient')
        const patient = JSON.parse(json)
        if(!patient){
            navigate(RoutesConstant.PATIENT.DASHBOARD)
        }
        setPatient(patient)
    }, []);

    return(
        <>
            <nav className="fixed top-0 z-50 w-full bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700">
                <Header patient={patient}/>
            </nav>
            <aside
                id="logo-sidebar"
                className="fixed top-5 left-0 z-40 w-64 h-screen pt-20 transition-transform -translate-x-full bg-white border-r border-gray-200 sm:translate-x-0 dark:bg-gray-800 dark:border-gray-700"
                aria-label="Sidebar">
                <div className="h-full px-3 pb-4 overflow-y-auto bg-white dark:bg-gray-800">
                    <SideBar patient={patient} />
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

export default PatientLayout