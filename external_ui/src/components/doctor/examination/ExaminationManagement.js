import styles from "../../../layouts/body/style.module.css";
import {useState} from "react";
import {WaitingExaminationPatientList} from "./WaitingExaminationPatientList";

const ExaminationManagement = () => {
    const [selectedTab, setSelectedTab] = useState(1)
    console.log(selectedTab)

    return (
        <div>
            <h2>Quản lý khám chữa bệnh</h2>

            <div className={styles.divFlex}>
                <div className={`${styles.cursorPointer}`} onClick={() => setSelectedTab(1)}>Danh sách bệnh nhân chờ khám</div>
                <div className={`${styles.cursorPointer}`} onClick={() => setSelectedTab(2)}>Danh sách bệnh nhân đã khám</div>
            </div>

            {selectedTab === 1 && <WaitingExaminationPatientList />}
        </div>
    )
}

export {ExaminationManagement}