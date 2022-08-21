import os, shutil, getpass

def error(msg):
    print('ERROR: %s'%msg)
    exit()
def info(msg):
    print('INFO: %s'%msg)

print('\n===========================================\nSETUP alpha STARTED: Welcome :)\n===========================================\n')

answer = None
while not answer:
    working_dir     = os.getcwd()
    raw_answer      = input(f'Setup project in <{working_dir}> directory ? (y/n) ').upper()

    answer          = raw_answer == 'Y'
    if raw_answer == 'N':
        working_dir = input('Working directory> ')
        answer      = os.path.exists(working_dir)
        if not answer:
            print('Directory does not exist')
    if raw_answer == "E":
        exit()

# setup directory
setup_dir       = working_dir + os.sep + 'setup'
if not os.path.exists(setup_dir):
    error('Missing setup directory')

# vscode directory
if not os.path.exists(working_dir + os.sep + '.vscode'):
    os.mkdir('.vscode')

# setup files
setup_files = {
    'launch.json':{'src':'','dst':'/.vscode'},
    'settings.json':{'src':'','dst':'/.vscode'},
    'user.json':{'src':'','dst':'/'}
}
for filename, config in setup_files.items():
    src, dst = config['src'], config['dst']

    if not os.path.exists(working_dir + dst + os.sep + filename):
        src_path, dst_path = setup_dir + src + os.sep + filename, working_dir + dst + os.sep + filename
        info('Copy %s to %s'%(src_path, dst_path))
        shutil.copyfile(src_path,dst_path)

# pip requirements

os.system('python -m pip install --trusted-host=pypi.python.org --trusted-host=pypi.org --trusted-host=files.pythonhosted.org -r requirements.txt')

# user folder
user = getpass.getuser()
root = os.getcwd().split(os.sep)[0]
user_path = root + os.sep + 'alpha' + os.sep + user
if not os.path.exists(user_path):
    os.makedirs(user_path)

print('\n===========================================\nSETUP alpha ENDED ! Enjoy ;)\n===========================================\n')