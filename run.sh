#!/usr/bin/env sh

error() {
  echo -e "\e[31mFAILED\e[0m"
}

pc() {
  program="$1"
  basename="${program%%.*}"

  nxjpcc "$program" ${@:2} \
   && nxjpc "$basename" \
   || error
}

up() {
  program="$1"
  basename="${program%%.*}"

  nxjc "$program" ${@:2} \
    && nxj -o "${basename}.nxj" "${basename}" \
    || error
}

graph() {
  git log --oneline --graph --all
}
