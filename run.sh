#!/usr/bin/env sh

error() {
  echo -e "\e[31mFAILED\e[0m"
}

pc() {
  program="$1"
  basename="${program%%.*}"

  nxjpcc "$program" \
   && nxjpc "$basename" \
   || error
}

up() {
  program="$1"
  basename="${program%%.*}"

  nxjc "$program" \
    && nxj -o "${basename}.nxj" "${basename}" ${@:2} \
    || error
}

graph() {
  git log --oneline --graph --all
}
