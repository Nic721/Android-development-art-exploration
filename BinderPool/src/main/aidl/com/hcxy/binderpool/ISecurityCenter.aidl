// ISecurityCenter.aidl
package com.hcxy.binderpool;

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
