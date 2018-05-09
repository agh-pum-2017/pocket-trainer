#!/usr/bin/env python

"""
On-line repository of training programs for Pocket Trainer mobile app.

Public access:
http://asdf.pythonanywhere.com/pocket-trainer

Usage:
$ python repository.py

  or

$ FLASK_APP=repository.py flask run --host=0.0.0.0 --port=8080

Setup:
$ pip install Pillow flask

Make sure there are ZIP archives in the same directory with the training
programs conforming to the expected JSON format.
"""

import os
import zipfile
import json
import base64
import io

from PIL import Image
from flask import Flask, Response, send_from_directory, abort

app = Flask(__name__)

DIRECTORY = '/home/asdf/pocket-trainer'
VERSION = '1.0'
THUMBNAIL_SIZE = (128, 128)


def list_files():
    """Return a list of ZIP filenames in the current directory."""
    return [x for x in os.listdir(DIRECTORY) if x.endswith('.zip')]


def make_thumbnail(archive, metadata):
    """Return Base64-encoded str or None."""
    image_path = metadata.get('image')
    if image_path is not None:
        image_bytes = archive.read(image_path)
        image = Image.open(io.BytesIO(image_bytes))
        image.thumbnail(THUMBNAIL_SIZE, Image.ANTIALIAS)
        image_bytes = io.BytesIO()
        image.save(image_bytes, format='PNG')
        return str(base64.b64encode(image_bytes.getvalue()))[2:-1]


def programs_index():
    """Return a JSON string with the available programs."""
    print('Scanning directory for ZIP archives...')
    programs = []
    for filename in list_files():
        with zipfile.ZipFile(os.path.join(DIRECTORY, filename), 'r') as archive:
            contents = json.loads(archive.read('program.json'))
            metadata = contents['program']['metadata']
            metadata.update(image=make_thumbnail(archive, metadata))
            programs.append({
                'format_version': contents['format_version'],
                'metadata': metadata,
                'filename': filename
            })
    return json.dumps({
        'format_version': VERSION,
        'programs': programs
    })


@app.route("/pocket-trainer")
def index():
    return Response(programs_index(), mimetype='application/json; charset=utf-8')


@app.route('/pocket-trainer/<path:path>')
def program(path):
    if path != __file__:
        return send_from_directory(DIRECTORY, path)
    abort(404)
