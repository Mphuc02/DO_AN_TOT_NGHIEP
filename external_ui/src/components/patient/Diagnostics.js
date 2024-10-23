import {useState, useEffect, useRef} from "react";
import styles from '../../layouts/body/style.module.css'
import {AI, HOSPITAL_INFORMATION} from "../../Constant";
import {SendApiService} from "../../service/SendApiService";

let page = ''
const Diagnostics = () => {
    page = 'send-image'

    const fileInputRef = useRef(null)
    const [imageDetails, setImageDetails] = useState([])
    const [selectedImage, setSelectedImage] = useState(null);
    const [diseases, setDiseases] = useState(new Map())
    const [detectedDiseases, setDetectedDiseases] = useState(new Set())

    const successSendImage = (response) => {
        const data = response.data
        console.log(data)
        imageDetails[0].processedImage = 'data:image/jpeg;base64,' + data.decodedImg
        setImageDetails([...imageDetails])

        data.diseases.forEach(detected => {
            detectedDiseases.add(detected)
        })
        setDetectedDiseases(detectedDiseases)
        setSelectedImage(null)
        fileInputRef.current.value = ''
    }

    const errorSendImage = (error) => {

    }

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file && file.type.startsWith('image/')) {
            const imageDetail = {
                processedImage: '',
                previewImage: URL.createObjectURL(file)
            }
            setSelectedImage(file);
            imageDetails[0] = imageDetail
        } else {
            alert("Please select a valid image file");
            setSelectedImage(null);
        }
    };

    const handleAddImage = () => {
        console.log('images', imageDetails)

        if(imageDetails.length === 0 || imageDetails[0].processedImage === null || imageDetails[0].processedImage === ''){
            alert("Lỗi: Ảnh hiện tại chưa được chuẩn đoán, không thể thêm 1 ảnh mới")
            return;
        }

        const newImage = {
            processedImage: '',
            previewImage: ''
        }
        imageDetails.unshift(newImage)
        setImageDetails([...imageDetails])
        setSelectedImage(null)
    }

    const handleDiagnosticButton = async () => {
        if(selectedImage === null){
            alert("Lỗi: Ảnh không được bỏ trống")
        }else{
            const formData = new FormData()
            formData.append("image", selectedImage)
            await SendApiService.postRequest(AI.diagnostic(), formData, {}, successSendImage, errorSendImage)
        }
    }

    const getAllDiseases = async () => {
        await SendApiService.getRequest(HOSPITAL_INFORMATION.DISEASES.getUrl(), {}, (response => {
            const tempMap = new Map()
            response.data.forEach(disease => {
                tempMap.set(disease.name, disease)
            })
            console.log(tempMap)
            setDiseases(tempMap)
        }), (error) => {

        })
    }

    useEffect(() => {
        getAllDiseases()
    }, [page])

    return (
        <div>
            <h2>Chuẩn đoán bệnh thông qua hình ảnh</h2>

            <input
                type="file"
                ref={fileInputRef}
                accept="image/*"
                onChange={handleImageChange}
            />

            <button onClick={() => handleDiagnosticButton()}>Chuẩn đoán</button> <br/>
            <button onClick={() => handleAddImage()}>Thêm ảnh</button>

            {imageDetails.map((image, index) => (
                <div key={index}>
                    <div className={styles.divFlex}>
                        <div>
                            <p>Ảnh đã chọn</p>
                            {imageDetails[index].previewImage && (
                                <img className={styles.previewImage} src={imageDetails[index].previewImage}
                                     alt="Ảnh đã chọn"/>
                            )}
                        </div>

                        <div>
                            <p>Kết quả chuẩn đoán</p>
                            {imageDetails[index].processedImage && (
                                <img className={styles.previewImage} src={imageDetails[index].processedImage}
                                     alt="Ảnh đã chọn"/>
                            )}
                        </div>

                        <button>Xóa ảnh</button>
                    </div>
                </div>
            ))}

            {/*<div className={styles.divFlex}>*/}
            {/*    <div>*/}
            {/*        <p>Ảnh đã chọn</p>*/}
            {/*        {selectedImage && (*/}
            {/*            <img className={styles.previewImage} src={previewImage} alt="Ảnh đã chọn"/>*/}
            {/*        )}*/}
            {/*    </div>*/}

            {/*    <div>*/}
            {/*        <p>Kết quả chuẩn đoán</p>*/}
            {/*        {processedImage && (*/}
            {/*            <img className={styles.previewImage} src={processedImage} alt="Ảnh đã chọn"/>*/}
            {/*        )}*/}
            {/*    </div>*/}

            {/*    <button>Xóa ảnh</button>*/}
            {/*</div>*/}

            {detectedDiseases.size > 0 && (
                <div>
                    <div>Dưới đây là danh sách các bệnh phát hiện được</div>
                    <ul>
                        {[...detectedDiseases].map(key => (
                            <li key={key}>{diseases.get(key).name}</li>
                        ))}
                    </ul>

                    <button>Tạo lịch hẹn khám bệnh</button>
                </div>)}
        </div>
    )
}

export {Diagnostics}