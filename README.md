# golden-kestrel

The Golden Kestrel manipulates donuts for the good of science.

This project is written in
[ClojureScript](https://github.com/clojure/clojurescript) and uses
[Om](https://github.com/swannodette/om/) and therefore
[ReactJS](http://facebook.github.io/react/).

This project was introduced with a
[blog post](http://www.altmetric.com/blog/bake-your-own-donuts/) on the
Altmetric blog.

## Usage

You will need [Leiningen](https://github.com/technomancy/leiningen/) installed
to work on the code in this repository, please refer to the [installation
instructions](https://github.com/technomancy/leiningen/#installation)

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
