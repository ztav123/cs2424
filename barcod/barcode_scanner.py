import cv2
from pyzbar.pyzbar import decode
import pyautogui
import time
import winsound  # <--- CHÚ Ý: Thêm thư viện này ở đầu file

def play_beep():
    # Tần số 2000Hz, kéo dài 150 mili-giây (mức chuẩn giống siêu thị)
    winsound.Beep(2000, 150)

def main():
    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        print("Không thể mở camera!")
        return

    cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

    print("\n=== HỆ THỐNG POS BARCODE X32 ĐANG HOẠT ĐỘNG ===")
    print("Vui lòng nhấp chuột chọn ô nhập liệu trên ứng dụng JavaFX WebView.")
    print("Nhấn phím 'q' tại cửa sổ camera để thoát ứng dụng.")
    
    scanned_barcodes = {}
    COOLDOWN_TIME = 0.67  

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        cv2.putText(frame, "STATUS: PYZBAR SCANNING...", (10, 30), 
                    cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)

        barcodes = decode(frame)

        for barcode in barcodes:
            try:
                barcode_data = barcode.data.decode('utf-8').strip()
                if not barcode_data:
                    continue

                current_time = time.time()
                last_scan_time = scanned_barcodes.get(barcode_data, 0)

                if current_time - last_scan_time > COOLDOWN_TIME:
                    print(f"[SUCCESS] Đã quét cực nhạy mã: {barcode_data}")
                    scanned_barcodes[barcode_data] = current_time

                    # === PHÁT TIẾNG BÍP TẠI ĐÂY ===
                    play_beep()
                    # ==============================

                    pts = barcode.polygon
                    if len(pts) == 4:
                        pts_list = [(pts[i].x, pts[i].y) for i in range(4)]
                        cv2.polylines(frame, [__import__('numpy').array(pts_list, __import__('numpy').int32)], True, (0, 255, 0), 3)

                    payload = barcode_data + "\\"
                    pyautogui.write(payload)

            except Exception as e:
                continue

        cv2.imshow('POS Scanner Camera (x32 Mode)', frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()