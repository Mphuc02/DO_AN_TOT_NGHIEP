import styles from "../../../layouts/body/style.module.css";
import {useState} from "react";
import {WaitingExaminationPatientList} from "./WaitingExaminationPatientList";

const ExaminationManagement = () => {
    const [selectedTab, setSelectedTab] = useState(1)
    console.log(selectedTab)

    return (
        <div>
            <h2 className="text-xl font-bold text-green-600 mb-4">Quản lý khám chữa bệnh</h2>
            <div className="flex space-x-4 mb-4">
                <div
                    className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 1 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(1)}>
                    Danh sách bệnh nhân chờ khám
                </div>
                <div
                    className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 2 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSelectedTab(2)}>
                    Danh sách bệnh nhân đã khám
                </div>
            </div>

            {selectedTab === 1 && <WaitingExaminationPatientList/>}
        </div>
    )
}

export {ExaminationManagement}