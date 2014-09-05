/*
 * Copyright 2014 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.async;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Created by petterwork on 04/09/14.
 */
public class RequestProxy implements HttpServletRequest {

    private final HttpSession session;

    /**
     * @param session
     */
    public RequestProxy(HttpSession session) {
        this.session = session;
    }

    private static UnsupportedOperationException notSupported() {
        return new UnsupportedOperationException("Operation not available on request proxy. Please invoke it on a real request.");
    }

    @Override
    public String getAuthType() {
        throw notSupported();
    }

    @Override
    public Cookie[] getCookies() {
        throw notSupported();
    }

    @Override
    public long getDateHeader(String name) {
        throw notSupported();
    }

    @Override
    public String getHeader(String name) {
        throw notSupported();
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        throw notSupported();
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        throw notSupported();
    }

    @Override
    public int getIntHeader(String name) {
        throw notSupported();
    }

    @Override
    public String getMethod() {
        throw notSupported();
    }

    @Override
    public String getPathInfo() {
        throw notSupported();
    }

    @Override
    public String getPathTranslated() {
        throw notSupported();
    }

    @Override
    public String getContextPath() {
        throw notSupported();
    }

    @Override
    public String getQueryString() {
        throw notSupported();
    }

    @Override
    public String getRemoteUser() {
        throw notSupported();
    }

    @Override
    public boolean isUserInRole(String role) {
        throw notSupported();
    }

    @Override
    public Principal getUserPrincipal() {
        throw notSupported();
    }

    @Override
    public String getRequestedSessionId() {
        throw notSupported();
    }

    @Override
    public String getRequestURI() {
        throw notSupported();
    }

    @Override
    public StringBuffer getRequestURL() {
        throw notSupported();
    }

    @Override
    public String getServletPath() {
        throw notSupported();
    }

    @Override
    public HttpSession getSession(boolean create) {
        return session;
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw notSupported();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw notSupported();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw notSupported();
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw notSupported();
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        throw notSupported();
    }

    @Override
    public void login(String username, String password) throws ServletException {
        throw notSupported();
    }

    @Override
    public void logout() throws ServletException {
        throw notSupported();
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw notSupported();
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        throw notSupported();
    }

    @Override
    public Object getAttribute(String name) {
        throw notSupported();
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw notSupported();
    }

    @Override
    public String getCharacterEncoding() {
        throw notSupported();
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        throw notSupported();
    }

    @Override
    public int getContentLength() {
        throw notSupported();
    }

    @Override
    public String getContentType() {
        throw notSupported();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw notSupported();
    }

    @Override
    public String getParameter(String name) {
        throw notSupported();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        throw notSupported();
    }

    @Override
    public String[] getParameterValues(String name) {
        throw notSupported();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        throw notSupported();
    }

    @Override
    public String getProtocol() {
        throw notSupported();
    }

    @Override
    public String getScheme() {
        throw notSupported();
    }

    @Override
    public String getServerName() {
        throw notSupported();
    }

    @Override
    public int getServerPort() {
        throw notSupported();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw notSupported();
    }

    @Override
    public String getRemoteAddr() {
        throw notSupported();
    }

    @Override
    public String getRemoteHost() {
        throw notSupported();
    }

    @Override
    public void setAttribute(String name, Object o) {
        throw notSupported();
    }

    @Override
    public void removeAttribute(String name) {
        throw notSupported();
    }

    @Override
    public Locale getLocale() {
        throw notSupported();
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw notSupported();
    }

    @Override
    public boolean isSecure() {
        throw notSupported();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw notSupported();
    }

    @Override
    public String getRealPath(String path) {
        throw notSupported();
    }

    @Override
    public int getRemotePort() {
        throw notSupported();
    }

    @Override
    public String getLocalName() {
        throw notSupported();
    }

    @Override
    public String getLocalAddr() {
        throw notSupported();
    }

    @Override
    public int getLocalPort() {
        throw notSupported();
    }

    @Override
    public ServletContext getServletContext() {
        throw notSupported();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw notSupported();
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        throw notSupported();
    }

    @Override
    public boolean isAsyncStarted() {
        throw notSupported();
    }

    @Override
    public boolean isAsyncSupported() {
        throw notSupported();
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw notSupported();
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw notSupported();
    }
}
