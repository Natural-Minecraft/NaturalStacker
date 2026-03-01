import os

def replace_in_file(file_path, search_text, replace_text):
    if not os.path.isfile(file_path):
        return
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            content = file.read()
    except UnicodeDecodeError:
        return # Skip binary files
    
    if search_text in content:
        new_content = content.replace(search_text, replace_text)
        with open(file_path, 'w', encoding='utf-8', newline='') as file:
            file.write(new_content)
        print(f"Updated: {file_path}")

def main():
    root_dir = r'd:\NaturalSMP\plugin\NaturalStacker'
    search_text = 'com.bgsoftware.common'
    replace_text = 'id.naturalsmp.common'
    
    for root, dirs, files in os.walk(root_dir):
        if 'build' in root or '.git' in root or '.gradle' in root:
            continue
        for file in files:
            if file.endswith('.java') or file.endswith('.template'):
                replace_in_file(os.path.join(root, file), search_text, replace_text)

if __name__ == "__main__":
    main()
