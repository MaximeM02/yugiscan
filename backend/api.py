#!flask/bin/python

import argparse

if __name__ == '__main__':
    parser = argparse.ArgumentParser(prog='Alpha', description='Api')
    parser.add_argument('--configuration', '-c', help='Set configuration')
    parser.add_argument('--stop', '-k', action='store_true', help='Stop api')
    parser.add_argument('--ini', '-i', action='store_true', help='Initiate a database')

    args = parser.parse_args()

    if args.configuration is not None:
        core.config.set_configuration(args.configuration)

    from core import core

    core.prepare_api(configuration=core.config.configuration)

    core.api.init()

    if args.stop:
        core.api.stop()
    else:
        core.api.start()
