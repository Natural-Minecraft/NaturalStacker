import os

def replace_in_file(file_path, replacements):
    if not os.path.isfile(file_path):
        return
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            content = file.read()
    except UnicodeDecodeError:
        return # Skip binary files if any
    
    original_content = content
    for search_text, replace_text in replacements:
        content = content.replace(search_text, replace_text)
    
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8', newline='') as file:
            file.write(content)
        print(f"Updated: {file_path}")

def main():
    base_dir = r'd:\NaturalSMP\plugin\NaturalStacker'
    nms_dir = os.path.join(base_dir, 'NMS')
    
    replacements = [
        ('com.bgsoftware.wildstacker', 'id.naturalsmp.naturalstacker'),
        ('WildStacker', 'NaturalStacker')
    ]
    
    # 1. Update properties files, templates, and Java files in NMS
    for root, dirs, files in os.walk(nms_dir):
        if 'build' in root:
            continue
        for file in files:
            if file.endswith(('.properties', '.template', '.java')) or file == 'properties':
                file_path = os.path.join(root, file)
                replace_in_file(file_path, replacements)

if __name__ == "__main__":
    main()
