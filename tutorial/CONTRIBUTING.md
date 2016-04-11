Contributing to the TicketMonster tutorial
==========================================

Each section of the tutorial is contained in a `.asciidoc` file in this git repository. To build the tutorial you'll need:

* [AsciiDoc](http://www.methods.co.nz/asciidoc/index.html) installed. It's available via most major packaging systems (e.g. Debian, Fedora Extra, MacPorts), and has a Windows installer.
* You'll also need pygments for syntax highlighting. It's available as a python egg. Easiest to install via `easy_install` e.g. `sudo easy_install pygments`.
* dblatex, once again available in most major packaging systems, or as a python egg e.g.  `sudo easy_install dblatex`
* asciidoctor, also available in most major packaging systems, or as a ruby gem -- as explained at http://asciidoctor.org/docs/install-toolchain/

Once you have installed AsciiDoc, you can build individual sections by invoking `asciidoc -b <output of choice> <section.txt>`. If you want to generate the whole tutorial as html, you can call `./generate-guides.sh`.
