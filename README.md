# golden-kestrel

The Golden Kestrel manipulates donuts for the good of science.

## Usage

To fetch required Javascript components:

    lein bower

To start automatic compilation:

    lein cljsbuild auto

The optimized compiled source will then be available in `kestrel-release.js`.

The `index.html` contains local resources, so can be used simply by opening it.
The `index-release.html` uses live assets including the compiled minified
`kestrel-release.js`. To use it, you'd have to fetch it from a webserver
(because most browsers won't allow `file:`-based resources to load Javascript
over HTTP(S)):

    python -m SimpleHTTPServer

## License

Copyright © 2014 Altmetric

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
