# golden-kestrel

The Golden Kestrel manipulates donuts for the good of science.

## Usage

To fetch required JavaScript components:

    lein bower

To start automatic compilation:

    lein cljsbuild auto

The optimized compiled source will then be available in `golden-kestrel.js`.

The `index.html` contains local resources, so can be used simply by opening it.
The `index-release.html` uses live assets including the compiled minified
`golden-kestrel.js`. To use it, you'd have to fetch it from a web server
(because most browsers won't allow `file:`-based resources to load JavaScript
over HTTP(S)):

    python -m SimpleHTTPServer

## License

Copyright © 2014 Altmetric

Distributed under the MIT license (see the LICENSE file).
