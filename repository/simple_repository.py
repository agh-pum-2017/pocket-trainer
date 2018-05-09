#/usr/bin/env python

"""
On-line repository of training programs for Pocket Trainer mobile app.

Usage:
$ python repository.py

Setup:
$ pip install Pillow

Make sure there are ZIP archives in the same directory with the training
programs conforming to the expected JSON format.
"""

import os
import zipfile
import json
import base64
import http.server
import io
import shutil

from PIL import Image


VERSION = '1.0'
HOST = '0.0.0.0'
PORT = 8080
ROOT_CONTEXT = '/repository'
THUMBNAIL_SIZE = (128, 128)


def list_files():
    """Return a list of ZIP filenames in the current directory."""
    return [x for x in os.listdir() if x.endswith('.zip')]


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
        with zipfile.ZipFile(filename, 'r') as archive:
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


def start_server():
    """Spin up an HTTP server."""

    class RequestHandler(http.server.BaseHTTPRequestHandler):
        """Custom handler for HTTP requests."""

        def do_GET(self):
            """Handle all incoming HTTP GET requests."""
            if self.path == ROOT_CONTEXT:
                self._send_response(programs_index())
            elif self.path.startswith(ROOT_CONTEXT):
                filename = os.path.basename(self.path)
                if os.path.exists(filename):
                    self._send_file(filename)
                else:
                    self._send_error(404, 'Not Found')
            else:
                self._send_error(400, 'Bad Request')

        def _send_response(self, content, content_type='application/json; charset=utf-8'):
            """Return HTTP 200 OK with content."""
            self.send_response(200)
            self.send_header('Content-Type', content_type)
            self.end_headers()
            self.wfile.write(content.encode('utf-8'))

        def _send_file(self, filename):
            with open(filename, 'rb') as fp:
                self.send_response(200)
                self.send_header('Content-Type', 'application/octet-stream')
                self.send_header('Content-Disposition', f'attachment; filename="{filename}"')
                file_stat = os.fstat(fp.fileno())
                self.send_header('Content-Length', str(file_stat.st_size))
                self.end_headers()
                shutil.copyfileobj(fp, self.wfile)

        def _send_error(self, code, message):
            """Return HTTP error message."""
            self.send_response(code)
            self.send_header('Content-Type', 'text/plain; charset=utf-8')
            self.end_headers()
            self.wfile.write(f'{code} {message}'.encode('utf-8'))

    server = http.server.HTTPServer((HOST, PORT), RequestHandler)
    print(f'Server listening at {HOST}:{PORT}...')
    server.serve_forever()


def main():
    """Application entry point."""
    start_server()


if __name__ == '__main__':
    main()
