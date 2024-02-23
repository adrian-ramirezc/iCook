import base64


def encode_image_to_base64(image_path: str) -> str:
    with open(image_path, "rb") as img_file:
        img_binary_data = img_file.read()
        base64_encoded_image = base64.b64encode(img_binary_data)
        return base64_encoded_image.decode("utf-8")
