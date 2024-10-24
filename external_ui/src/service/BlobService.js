const ConvertBlobUrlToBase64 = async (blobUrl) => {
    try {
        // Sử dụng fetch để lấy Blob từ URL
        const response = await fetch(blobUrl);
        const blob = await response.blob();

        // Chuyển Blob thành chuỗi Base64
        const base64String = await convertBlobToBase64(blob);
        return base64String;
    } catch (error) {
        console.error("Lỗi khi chuyển Blob URL sang Base64:", error);
        return null;
    }
}

function convertBlobToBase64(blob) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();

        reader.onloadend = () => {
            resolve(reader.result);  // Chuỗi Base64
        };

        reader.onerror = (error) => {
            reject(error);  // Bắt lỗi nếu có
        };

        // Bắt đầu đọc Blob dưới dạng Base64
        reader.readAsDataURL(blob);
    });
}

export {ConvertBlobUrlToBase64}
