import cv2
import numpy as np
from cvzone.SelfiSegmentationModule import SelfiSegmentation
from PIL import Image
import io
import urllib

def url_to_image(url):
    resp = urllib.request.urlopen(url)
    image = np.asarray(bytearray(resp.read()), dtype="uint8")
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)
    return image

def main(img, url):
    bg_image = url_to_image(url)
    bg_image = cv2.cvtColor(bg_image, cv2.COLOR_BGR2RGB)

    selfie_segmentation = SelfiSegmentation()

    decode_data = base64.b64decode(img)
    np_data = np.fromstring(decode_data, np.uint8)
    img = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)
    img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    h, w, c = img_rgb.shape

    result = selfie_segmentation.process(img_rgb)

    condition = np.stack(
        (result.segmentation_mask,) * 3, axis=-1
    ) > 0.5

    bg_image = cv2.resize(bg_image, (w, h))
    output_img = np.where(condition, img_rgb, bg_image)

    pil_im = Image.fromarray(output_img)
    buff = io.BytesIO()
    pil_im.save(buff, format="PNG")
    img_str = base64.b64encode(buff.getvalue())
    return ""+str(img_str, 'utf-8')