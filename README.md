Dev Arsenal
============

Dev swiss army knife CLI, instead of waiting for [Dev Toys](https://github.com/veler/DevToys) to be ported to macos

Dependency on macos because of `pbcopy`.

Installation
------------
1. Install [babashka](https://github.com/babashka/babashka#installation)
2. Copy `dev_arsenal.clj` to wherever on your path like `/usr/local/bin`
3. Add `alias ars="bb dev_arsenal.clj`  to `~/.zshrc`  

Commands
--------

`ars uuid`

Generate a random uuid4.

`ars pw [length]`

Generate a random password with special characters. Length is optional, defaults to 12.

`ars b64d [txt]`

Decode from base 64.

`ars b64e [txt]`

Encode to base 64.

`ars hex2rgb [code]`

convert hex color code to rgb

`ars rgb2hex [red] [green] [blue]`

convert rgb color code to hex