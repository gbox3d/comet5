import { Template } from 'meteor/templating';
import { ReactiveVar } from 'meteor/reactive-var';

import {comet5} from "./comet5";

import './main.html';

Template.hello.onCreated(function helloOnCreated() {
  // counter starts at 0
  this.counter = new ReactiveVar(0);
  this.comet5 = new comet5()
});

Template.hello.helpers({
  counter() {
    return Template.instance().counter.get();
  },
});

Template.hello.events({

  'click [name="dummyWIO"] button'(event, instance) {

    console.log('test')

    instance.comet5.dummyWIO.setCallback(
      {
        OnCallback : ()=> {
          console.log('hello comet5')
          instance.counter.set(instance.counter.get() + 1);
        }
      }
    )
    instance.comet5.dummyWIO.Test()
  },
  'click [name="WIOgps"]'(event, instance) {

    instance.comet5.WIOgps.setCallback({
      OnCallback : (_)=> {
        console.log(_)

      }
    })
    instance.comet5.WIOgps.getLastLocation()

  }
});
