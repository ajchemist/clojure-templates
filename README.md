---
title: io.github.ajchemist/clojure-templates
---


# `~/.clojure/deps.edn`


``` shell
{:aliases
 {:io.github.ajchemist/clojure-templates
  {:deps {io.github.ajchemist/clojure-templates {:git/sha "9ca115b479fc7f65c3245654351bec5ca8dd036c" #_:local/root "..."}}}}}
```


# Get Started


``` shell
clojure -Ttools install-latest :lib io.github.seancorfield/deps-new :as new
clojure -A:io.github.ajchemist/clojure-templates -Tnew create \
	:template ajchemist/scratch \
	:name scratch-example
```


You want to debug then


``` shell
clojure -J-Dio.github.ajchemist.templates.verbose=1 \
	-A:io.github.ajchemist/clojure-templates -Tnew create \
	:template ajchemist/scratch \
	:name scratch-example
```


# References


- <https://github.com/seancorfield/deps-new>
